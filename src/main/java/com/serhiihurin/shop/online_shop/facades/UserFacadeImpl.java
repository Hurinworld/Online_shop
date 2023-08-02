package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.UserRequestDTO;
import com.serhiihurin.shop.online_shop.dto.UserResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.request.RegisterRequest;
import com.serhiihurin.shop.online_shop.services.UserService;
import com.serhiihurin.shop.online_shop.services.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserFacadeImpl implements UserFacade {
    private final UserService userService;
    private final JWTService jwtService;
    private final ModelMapper modelMapper;

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
    public User createUser(RegisterRequest registerRequest) {
        return userService.createUser(registerRequest);
    }


    //TODO change this update to patch format //done
    //TODO reformat this method for facade style //done
    @Override
    public User updateUser(User currentAuthenticatedUser, UserRequestDTO userRequestDTO) {
        log.info("Updating client account information with id: {}", currentAuthenticatedUser.getId());
        return userService.updateUser(currentAuthenticatedUser, userRequestDTO);
    }

    //TODO work only with username in args //done
    @Override
    public UserResponseDTO updateUsername(User currentAuthenticatedUser, String email) {
        currentAuthenticatedUser = userService.updateUsername(currentAuthenticatedUser, email);

        UserResponseDTO clientResponseDTO = modelMapper.map(
                currentAuthenticatedUser, UserResponseDTO.class
        );

        clientResponseDTO.setAccessToken(jwtService.generateAccessToken(currentAuthenticatedUser));
        clientResponseDTO.setRefreshToken(jwtService.generateRefreshToken(currentAuthenticatedUser));

        log.info("Updating client account username with id: {}", currentAuthenticatedUser.getId());
        return clientResponseDTO;
    }

    @Override
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }
}
