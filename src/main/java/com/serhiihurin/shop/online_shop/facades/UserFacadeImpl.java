package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.UserRequestDTO;
import com.serhiihurin.shop.online_shop.dto.UserResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.request.RegisterRequest;
import com.serhiihurin.shop.online_shop.services.UserService;
import com.serhiihurin.shop.online_shop.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {
    private final UserService userService;
    private final JWTService jwtService;
    private final ModelMapper modelMapper;

    @Override
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public User getUser(Long id) {
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
    public User updateUser(UserRequestDTO userRequestDTO) {
        User oldUser = userService.getUser(userRequestDTO.getId());
        return userService.updateUser(userRequestDTO, oldUser);
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

        return clientResponseDTO;
    }

    @Override
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }
}
