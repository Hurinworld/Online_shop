package com.serhiihurin.shop.online_shop.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.facades.ClientFacade;
import com.serhiihurin.shop.online_shop.request.AuthenticationRequest;
import com.serhiihurin.shop.online_shop.request.RegisterRequest;
import com.serhiihurin.shop.online_shop.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
    //TODO check is it correct
    private final ClientFacade clientFacade;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        Client client = clientFacade.createClient(registerRequest);
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
        Client client = clientFacade.getClientByEmail(request.getEmail());
        String jwtToken = jwtService.generateAccessToken(client);
        String refreshToken = jwtService.generateRefreshToken(client);
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

        //TODO 'this' is redundant here
        Client client = this.clientFacade.getClientByEmail(clientEmail);

        String accessToken = jwtService.generateAccessToken(client);
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponse);
    }
}
