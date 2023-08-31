package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.*;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.facades.UserFacade;
import com.serhiihurin.shop.online_shop.services.EmailService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
public class UserRESTController {
    private final UserFacade userFacade;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Operation(
            description = "GET all users endpoint for admin",
            summary = "endpoint to retrieve info about all user accounts",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad syntax",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    )
            }
    )
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
    @Operation(
            description = "GET user endpoint for admin",
            summary = "endpoint to retrieve info about user account by ID",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad syntax",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    )
            },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID value of user needed for getting info from database",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin view info')")
    public UserResponseForAdminDTO getUser(@PathVariable Long id) {
        return modelMapper.map(userFacade.getUser(id), UserResponseForAdminDTO.class);
    }

    @Timed("user_info_endpoint")
    @Operation(
            description = "GET endpoint for all users",
            summary = "endpoint to retrieve info about self user account",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad syntax",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    )
            },
            parameters = {
                    @Parameter(
                            name = "currentAuthenticatedUser",
                            description = "Current authenticated user object",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            }
    )
    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('admin view info', 'client view info', 'shop owner view info')")
    public UserResponseDTO getUser(User currentAuthenticatedUser) {
        return modelMapper.map(
                userFacade.getUser(currentAuthenticatedUser.getId()), UserResponseDTO.class
        );
    }

    @Operation(
            description = "PATCH endpoint for all users",
            summary = "endpoint to update user account info",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad syntax",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    )
            },
            parameters = {
                    @Parameter(
                            name = "currentAuthenticatedUser",
                            description = "Current authenticated user object",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            },
            requestBody =
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "A request DTO with new account information",
                    required = true
            )
    )
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

    @Operation(
            description = "PUT endpoint for all users",
            summary = "endpoint to update username and retrieve new tokens",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad syntax",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    )
            },
            parameters = {
                    @Parameter(
                            name = "currentAuthenticatedUser",
                            description = "Current authenticated user object",
                            in = ParameterIn.QUERY,
                            required = true
                    ),
                    @Parameter(
                            name = "email",
                            description = "New email to set",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            }
    )
    @PutMapping("/info/username")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<UsernameUpdateResponseDTO> updateUsername(
            User currentAuthenticatedUser,
            @RequestParam String email
    ) {
        return ResponseEntity.ok(userFacade.updateUsername(currentAuthenticatedUser, email));
    }

    @Operation(
            description = "GET endpoint for getting verification code",
            summary = "endpoint to get verification code for password updating",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad syntax",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    )
            },
            parameters = {
                    @Parameter(
                            name = "currentAuthenticatedUser",
                            description = "Current authenticated user object",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            }
    )
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

    @Operation(
            description = "PUT endpoint for password updating",
            summary = "endpoint to set a new password",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad syntax",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    )
            },
            parameters = {
                    @Parameter(
                            name = "currentAuthenticatedUser",
                            description = "Current authenticated user object",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "A request DTO with verification code and new password",
                    required = true
            )
    )
    //TODO verbs in path //done
    @PutMapping("/info/password")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<Void> updatePassword(
            User currentAuthenticatedUser,
            @RequestBody PasswordUpdateRequestDTO passwordUpdateRequestDTO
    ) {
        userFacade.updatePassword(currentAuthenticatedUser, passwordUpdateRequestDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(
            description = "DELETE user endpoint for admin",
            summary = "endpoint for admin to delete user account by ID",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "Wrong path variable value",
                            responseCode = "400"
                    )
            },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID value of user needed for deleting info from database",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @DeleteMapping
    @PreAuthorize("hasAuthority('super admin info deletion')")
    public ResponseEntity<Void> deleteUser(@RequestParam(required = false) Long id) {
       //TODO move it to facade //done
//        if (id == null) {
//            throw new ApiRequestException("Invalid URL. Parameter id must not be null");
//        }
        userFacade.deleteUser(id);
        log.info("Super admin: deleted user with id: {}", id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            description = "DELETE self account endpoint for all users",
            summary = "endpoint for users to delete their own accounts",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    ),
            },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID value of user needed for deleting info from database",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @DeleteMapping("/my-account")
    @PreAuthorize("hasAuthority('account management')")
    public ResponseEntity<Void> deleteUser(User currentAuthenticatedUser) {
        userFacade.deleteUser(currentAuthenticatedUser.getId());
        log.info("Deleted client with id: {}", currentAuthenticatedUser.getId());
        return ResponseEntity.ok().build();
    }
}
