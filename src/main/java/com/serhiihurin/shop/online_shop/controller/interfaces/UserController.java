package com.serhiihurin.shop.online_shop.controller.interfaces;

import com.serhiihurin.shop.online_shop.dto.*;
import com.serhiihurin.shop.online_shop.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserController {
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
    @SuppressWarnings("unused")
    List<UserResponseForAdminDTO> getAllUsers();

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
    @SuppressWarnings("unused")
    UserResponseForAdminDTO getUser(@PathVariable Long id);

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
    @SuppressWarnings("unused")
    UserResponseDTO getUser(User currentAuthenticatedUser);

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
    @SuppressWarnings("unused")
    ResponseEntity<UserResponseDTO> updateUser(
            User currentAuthenticatedUser,
            @RequestBody UserRequestDTO userRequestDTO
    );

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
    @SuppressWarnings("unused")
    ResponseEntity<UsernameUpdateResponseDTO> updateUsername(
            User currentAuthenticatedUser,
            @RequestParam String email
    );

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
    @SuppressWarnings("unused")
    ResponseEntity<Void> updatePasswordRequest(
            User currentAuthenticatedUser
    );

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
    @SuppressWarnings("unused")
    ResponseEntity<Void> updatePassword(
            User currentAuthenticatedUser,
            @RequestBody PasswordUpdateRequestDTO passwordUpdateRequestDTO
    );

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
    @SuppressWarnings("unused")
    ResponseEntity<Void> deleteUser(@RequestParam(required = false) Long id);

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
    @SuppressWarnings("unused")
    ResponseEntity<Void> deleteUser(User currentAuthenticatedUser);
}
