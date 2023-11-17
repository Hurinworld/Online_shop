package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dao.VerificationCodeRepository;
import com.serhiihurin.shop.online_shop.dto.PasswordUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.dto.UserRequestDTO;
import com.serhiihurin.shop.online_shop.dto.UsernameUpdateResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.dto.RegisterRequestDTO;
import com.serhiihurin.shop.online_shop.entity.VerificationCode;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.exception.VerificationCodeExpirationException;
import com.serhiihurin.shop.online_shop.services.UserService;
import com.serhiihurin.shop.online_shop.services.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserFacadeImpl implements UserFacade {
    private final UserService userService;
    private final JWTService jwtService;
    private final ModelMapper modelMapper;
    private final VerificationCodeRepository verificationCodeRepository;
    @Value("${custom.verification-code-expiration}")
    private int expiration;

    @Override
    public List<User> getAllUsers() {
        log.info("Admin: getting all users info");
        return userService.getAllUsers();
    }

    @Override
    public User getUser(Long id) {
        log.info("Admin: getting client info");
        return userService.getUser(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    @Override
    public User createUser(RegisterRequestDTO registerRequestDTO) {
        return userService.createUser(registerRequestDTO);
    }


    @Override
    public User updateUser(User currentAuthenticatedUser, UserRequestDTO userRequestDTO) {
        log.info("Updating client account information with id: {}", currentAuthenticatedUser.getId());
        return userService.updateUser(currentAuthenticatedUser, userRequestDTO);
    }

    @Override
    public UsernameUpdateResponseDTO updateUsername(User currentAuthenticatedUser, String email) {
        currentAuthenticatedUser = userService.updateUsername(currentAuthenticatedUser, email);

        UsernameUpdateResponseDTO usernameUpdateResponseDTO = modelMapper.map(
                currentAuthenticatedUser, UsernameUpdateResponseDTO.class
        );

        usernameUpdateResponseDTO.setAccessToken(jwtService.generateAccessToken(currentAuthenticatedUser));
        usernameUpdateResponseDTO.setRefreshToken(jwtService.generateRefreshToken(currentAuthenticatedUser));

        log.info("Updating user account username with id: {}", currentAuthenticatedUser.getId());
        return usernameUpdateResponseDTO;
    }

    @Transactional
    @Override
    public void updatePassword(User currentAuthenticatedUser, PasswordUpdateRequestDTO passwordUpdateRequestDTO) {
        VerificationCode verificationCode =
                verificationCodeRepository.findByUserId(currentAuthenticatedUser.getId())
                        .orElseThrow(() -> new ApiRequestException("Missing verification code"));
        if (!verificationCode.getVerificationCode().equals(passwordUpdateRequestDTO.getVerificationCode())) {
            throw new ApiRequestException("Invalid verification code. Try again");
        }
        if (LocalDateTime.now().isAfter(verificationCode.getCreationTime().plusMinutes(expiration))) {
            throw new VerificationCodeExpirationException("Verification code is no longer valid");
        }
        verificationCodeRepository.deleteVerificationCodeByUserId(currentAuthenticatedUser.getId());
        userService.updatePassword(currentAuthenticatedUser,passwordUpdateRequestDTO);
    }


    @Override
    public void deleteUser(Long id) {
        if (id == null) {
            throw new ApiRequestException("Invalid URL. Parameter id must not be null");
        }
        log.info("Deleted client with id: {}", id);
        userService.deleteUser(id);
    }
}
