package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.UserRequestDTO;
import com.serhiihurin.shop.online_shop.dto.UserResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.request.RegisterRequest;

import java.util.List;

public interface UserFacade {
    List<User> getAllUsers();

    User getUser(Long id);

    User getUserByEmail(String email);

    User createUser(RegisterRequest registerRequest);

    User updateUser(UserRequestDTO userRequestDTO);

    UserResponseDTO updateUsername(User currentAuthenticatedUser, String email);

    void deleteUser(Long id);
}
