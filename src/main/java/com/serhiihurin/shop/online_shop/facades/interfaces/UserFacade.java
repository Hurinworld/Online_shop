package com.serhiihurin.shop.online_shop.facades.interfaces;

import com.serhiihurin.shop.online_shop.dto.PasswordUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.dto.UserRequestDTO;
import com.serhiihurin.shop.online_shop.dto.UsernameUpdateResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;

import java.util.List;

public interface UserFacade {
    List<User> getAllUsers();

    User getUser(Long id);

    User getUserByEmail(String email);

    User updateUser(User currentAuthenticatedUser, UserRequestDTO userRequestDTO);

    UsernameUpdateResponseDTO updateUsername(User currentAuthenticatedUser, String email);

    void updatePassword(User currentAuthenticatedUser, PasswordUpdateRequestDTO passwordUpdateRequestDTO);

    void deleteUser(Long id);
}
