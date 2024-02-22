package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.controller.interfaces.AuthenticationController;
import com.serhiihurin.shop.online_shop.dto.AuthenticationRequestDTO;
import com.serhiihurin.shop.online_shop.dto.RegisterRequestDTO;
import com.serhiihurin.shop.online_shop.dto.AuthenticationResponseDTO;
import com.serhiihurin.shop.online_shop.facades.interfaces.AuthenticationFacade;
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
public class AuthenticationControllerImpl implements AuthenticationController {
    private final AuthenticationFacade authenticationFacade;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(
            @RequestBody RegisterRequestDTO request
    ) {
        return ResponseEntity.ok(authenticationFacade.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody AuthenticationRequestDTO request
    ) {
        AuthenticationResponseDTO response = authenticationFacade.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/access-token")
    public void updateAccessToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationFacade.updateAccessToken(request, response);
    }
}
