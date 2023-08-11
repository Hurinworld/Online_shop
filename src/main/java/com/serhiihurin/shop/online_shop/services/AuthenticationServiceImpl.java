package com.serhiihurin.shop.online_shop.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.request.AuthenticationRequest;
import com.serhiihurin.shop.online_shop.request.RegisterRequest;
import com.serhiihurin.shop.online_shop.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        log.info("Registering new client with email: {}", registerRequest.getEmail());
        User user = userService.createUser(registerRequest);
        String jwtToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        User user = userService.getUserByEmail(authenticationRequest.getEmail());
        String jwtToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        log.info("Log in to system with account email: {}", authenticationRequest.getEmail());
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void updateAccessToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String refreshToken = authHeader.substring("Bearer ".length());
        String clientEmail = jwtService.extractUsername(refreshToken);

        if (clientEmail == null) {
            return;
        }

        //TODO 'this' is redundant here //done
        User user = userService.getUserByEmail(clientEmail);

        String accessToken = jwtService.generateAccessToken(user);
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponse);
    }
}
