package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.AuthenticationRequestDTO;
import com.serhiihurin.shop.online_shop.dto.AuthenticationResponseDTO;
import com.serhiihurin.shop.online_shop.dto.RegisterRequestDTO;
import com.serhiihurin.shop.online_shop.facades.interfaces.AuthenticationFacade;
import com.serhiihurin.shop.online_shop.services.interfaces.AuthenticationService;
import com.serhiihurin.shop.online_shop.services.interfaces.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationFacadeImpl implements AuthenticationFacade {
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @Override
    public AuthenticationResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.register(registerRequestDTO);
        emailService.sendGreetingsEmail(registerRequestDTO.getEmail(), registerRequestDTO.getFirstName());
        return authenticationResponseDTO;
    }

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        return authenticationService.authenticate(request);
    }

    @Override
    public void updateAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.updateAccessToken(request, response);
    }
}
