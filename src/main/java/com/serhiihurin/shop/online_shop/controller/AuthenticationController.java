package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.AuthenticationRequestDTO;
import com.serhiihurin.shop.online_shop.dto.RegisterRequestDTO;
import com.serhiihurin.shop.online_shop.dto.AuthenticationResponseDTO;
import com.serhiihurin.shop.online_shop.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/online-shop/auth")
@Tag(name = "Authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

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
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(
            @RequestBody RegisterRequestDTO request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

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
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody AuthenticationRequestDTO request
    ) {
        AuthenticationResponseDTO response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

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
    @PostMapping("/access-token")
    public void updateAccessToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.updateAccessToken(request, response);
    }
}
