package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.request.AuthenticationRequest;
import com.serhiihurin.shop.online_shop.request.RegisterRequest;
import com.serhiihurin.shop.online_shop.response.AuthenticationResponse;
import com.serhiihurin.shop.online_shop.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/online-shop/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final Logger logger;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        logger.info("Registering new client with email: {}", request.getEmail());
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        logger.info("Log in to system with account email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/access-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.updateAccessToken(request, response);
    }
}
