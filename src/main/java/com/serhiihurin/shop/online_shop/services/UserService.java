package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dto.UserRequestDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.request.RegisterRequest;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUser(Long id);

    User getUserByEmail(String email);

    User createUser(RegisterRequest registerRequest);

    void saveUser(User user);

    User updateUser(UserRequestDTO userRequestDTO, User user);

    User updateUsername(User currenAuthenticatedUser, String email);

    void deleteUser(Long id);
}
