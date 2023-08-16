package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.UserRepository;
import com.serhiihurin.shop.online_shop.dto.UserRequestDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.request.RegisterRequestDTO;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Timed("getting_user")
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Could not find user"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getByEmail(email)
                .orElseThrow(() -> new ApiRequestException("Could not find user"));
    }

    @Override
    public User createUser(RegisterRequestDTO registerRequestDTO) {
        User user = User.builder()
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .cash(registerRequestDTO.getCash())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .role(registerRequestDTO.getRole())
                .build();
        return userRepository.save(user);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User updateUser(User currentAuthenticatedUser, UserRequestDTO userRequestDTO) {
        if (userRequestDTO.getFirstName() != null) {
            currentAuthenticatedUser.setFirstName(userRequestDTO.getFirstName());
        }
        if (userRequestDTO.getLastName() != null) {
            currentAuthenticatedUser.setLastName(userRequestDTO.getLastName());
        }
        if (userRequestDTO.getCash() != null) {
            currentAuthenticatedUser.setCash(userRequestDTO.getCash());
        }
        if (userRequestDTO.getPassword() != null) {
            currentAuthenticatedUser.setPassword(userRequestDTO.getPassword());
        }

        return userRepository.save(currentAuthenticatedUser);
    }

    @Override
    public User updateUsername(User currenAuthenticatedUser, String email) {
        if (!currenAuthenticatedUser.getEmail().equals(email)) {
            currenAuthenticatedUser.setEmail(email);
        }
        return userRepository.save(currenAuthenticatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
