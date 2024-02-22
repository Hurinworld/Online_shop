package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.controller.interfaces.UserController;
import com.serhiihurin.shop.online_shop.dto.*;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.facades.interfaces.UserFacade;
import com.serhiihurin.shop.online_shop.services.interfaces.EmailService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/users")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'CLIENT')")
@Tag(name = "User")
@RequiredArgsConstructor
@Slf4j
public class UserControllerImpl implements UserController {
    private final UserFacade userFacade;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin view info')")
    public List<UserResponseForAdminDTO> getAllUsers() {
        return modelMapper.map(
                userFacade.getAllUsers(),
                new TypeToken<List<UserResponseForAdminDTO>>() {
                }.getType()
        );
    }

    @Timed("user_info_endpoint_admin")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin view info')")
    public UserResponseForAdminDTO getUser(@PathVariable Long id) {
        return modelMapper.map(userFacade.getUser(id), UserResponseForAdminDTO.class);
    }

    @Timed("user_info_endpoint")
    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('admin view info', 'client view info', 'shop owner view info')")
    public UserResponseDTO getUser(User currentAuthenticatedUser) {
        return modelMapper.map(
                userFacade.getUser(currentAuthenticatedUser.getId()), UserResponseDTO.class
        );
    }

    @PatchMapping("/info")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<UserResponseDTO> updateUser(
            User currentAuthenticatedUser,
            @RequestBody UserRequestDTO userRequestDTO
    ) {
        return ResponseEntity.ok(
                modelMapper.map(
                        userFacade.updateUser(currentAuthenticatedUser, userRequestDTO),
                        UserResponseDTO.class
                )
        );
    }

    @PutMapping("/info/username")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<UsernameUpdateResponseDTO> updateUsername(
            User currentAuthenticatedUser,
            @RequestParam String email
    ) {
        return ResponseEntity.ok(userFacade.updateUsername(currentAuthenticatedUser, email));
    }

    @GetMapping("/info/password/code")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<Void> updatePasswordRequest(
            User currentAuthenticatedUser
    ) {
        emailService.sendPasswordChangingVerificationCode(
                currentAuthenticatedUser.getEmail()
        );
        log.info("Password changing request from account: {}", currentAuthenticatedUser.getEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/info/password")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<Void> updatePassword(
            User currentAuthenticatedUser,
            @RequestBody PasswordUpdateRequestDTO passwordUpdateRequestDTO
    ) {
        userFacade.updatePassword(currentAuthenticatedUser, passwordUpdateRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('super admin info deletion')")
    public ResponseEntity<Void> deleteUser(@RequestParam(required = false) Long id) {
        userFacade.deleteUser(id);
        log.info("Super admin: deleted user with id: {}", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/my-account")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<Void> deleteUser(User currentAuthenticatedUser) {
        userFacade.deleteUser(currentAuthenticatedUser.getId());
        return ResponseEntity.ok().build();
    }
}
