package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ClientRepository;
import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.enums.Role;
import com.serhiihurin.shop.online_shop.request.AuthenticationRequest;
import com.serhiihurin.shop.online_shop.request.RegisterRequest;
import com.serhiihurin.shop.online_shop.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        return AuthenticationResponse.builder()
                .token(jwtToken)
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
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
