package com.serhiihurin.shop.online_shop.controller.interfaces;

import com.serhiihurin.shop.online_shop.dto.AuthenticationRequestDTO;
import com.serhiihurin.shop.online_shop.dto.AuthenticationResponseDTO;
import com.serhiihurin.shop.online_shop.dto.RegisterRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

public interface AuthenticationController {
    @Operation(
            description = "POST endpoint for new users",
            summary = "endpoint for new users to registering an account",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad request",
                            responseCode = "400"
                    ),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "A request DTO with account info to be saved",
                    required = true
            )
    )
    @SuppressWarnings("unused")
    ResponseEntity<AuthenticationResponseDTO> register(
            @RequestBody RegisterRequestDTO request
    );

    @Operation(
            description = "POST endpoint for all users",
            summary = "endpoint for all users to authenticate",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad request",
                            responseCode = "400"
                    ),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "A request DTO with account info used for authentication",
                    required = true
            )
    )
    @SuppressWarnings("unused")
    ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody AuthenticationRequestDTO request
    );

    @Operation(
            description = "POST endpoint for all users",
            summary = "endpoint for all users to update access token",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad request",
                            responseCode = "400"
                    ),
            }
    )
    @SuppressWarnings("unused")
    void updateAccessToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;
}
