package com.serhiihurin.shop.online_shop.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serhiihurin.shop.online_shop.dao.ClientRepository;
import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.request.AuthenticationRequest;
import com.serhiihurin.shop.online_shop.request.RegisterRequest;
import com.serhiihurin.shop.online_shop.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        Client client = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .cash(request.getCash())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        clientRepository.save(client);
        String jwtToken = jwtService.generateAccessToken(client);
        String refreshToken = jwtService.generateRefreshToken(client);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        Client client = clientRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateAccessToken(client);
        String refreshToken = jwtService.generateRefreshToken(client);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void refreshToken(
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

        Client client = this.clientRepository.findByEmail(clientEmail).orElseThrow();

//        if (!jwtService.isTokenValid(refreshToken, client)) {
//            return;
//        }

        String accessToken = jwtService.generateAccessToken(client);
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponse);
    }
}
