package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.UserResponseDTO;
import com.serhiihurin.shop.online_shop.dto.UserRequestDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.facades.UserFacade;
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
@RequiredArgsConstructor
@Slf4j
public class UserRESTController {
    private final UserFacade userFacade;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('admin view info')")
    public List<UserResponseDTO> getAllUsers() {
        return modelMapper.map(
                userFacade.getAllUsers(),
                new TypeToken<List<UserResponseDTO>>(){}.getType()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin view info')")
    public UserResponseDTO getUser(@PathVariable Long id) {
        return modelMapper.map(userFacade.getUser(id), UserResponseDTO.class);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('admin view info', 'client view info', 'shop owner view info')")
    //TODO remove model attribute //done
    public UserResponseDTO getUser(User currentAuthenticatedUser) {
        return modelMapper.map(
                userFacade.getUser(currentAuthenticatedUser.getId()), UserResponseDTO.class
        );
    }

    @PatchMapping("/info")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<UserResponseDTO> updateUser(User currentAuthenticatedUser,
                                                      @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(
                modelMapper.map(
                        userFacade.updateUser(currentAuthenticatedUser, userRequestDTO),
                        UserResponseDTO.class
                )
        );
    }

    @PutMapping("/info/username")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<UserResponseDTO> updateUsername(
            User currentAuthenticatedUser,
            @RequestParam String email
    ) {
        //TODO move logging to facade //done
        return ResponseEntity.ok(userFacade.updateUsername(currentAuthenticatedUser, email));
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('super admin info deletion')")
    public ResponseEntity<Void> deleteUser(@RequestParam(required = false) Long id) {
//        if (id == null) {
//            throw new ApiRequestException("Invalid URL");
//        }
        //FIXME 401 exception
//        if(2==2)
//        throw new RuntimeException();
//        User user = userFacade.getUser(id);
        log.info("got here");
        userFacade.deleteUser(id);
        log.info("Super admin: deleted user with id: {}", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/my-account")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<Void> deleteUser(User currentAuthenticatedUser) {
        userFacade.deleteUser(currentAuthenticatedUser.getId());
        log.info("Deleted client with id: {}", currentAuthenticatedUser.getId());
        return ResponseEntity.ok().build();
    }
}
