package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dto.PasswordUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.dto.UserRequestDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.dto.RegisterRequestDTO;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUser(Long id);

    User getUserByEmail(String email);

    User createUser(RegisterRequestDTO registerRequestDTO);

    void saveUser(User user);

    User updateUser(User currentAuthenticatedUser, UserRequestDTO userRequestDTO);

    User updateUsername(User currenAuthenticatedUser, String email);

    void updatePassword(User currentAuthenticatedUser, PasswordUpdateRequestDTO passwordUpdateRequestDTO);

    void deleteUser(Long id);
}
