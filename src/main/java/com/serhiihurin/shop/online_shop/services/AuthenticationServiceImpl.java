package com.serhiihurin.shop.online_shop.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.dto.AuthenticationRequestDTO;
import com.serhiihurin.shop.online_shop.dto.RegisterRequestDTO;
import com.serhiihurin.shop.online_shop.dto.AuthenticationResponseDTO;
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
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        User user = userService.createUser(registerRequestDTO);
        String jwtToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        //TODO this should be in facade, not auth service //done
//        emailService.sendGreetingsEmail(registerRequestDTO.getEmail(), registerRequestDTO.getFirstName());
        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userService.getUserByEmail(request.getEmail());
        String jwtToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponseDTO.builder()
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

        User user = userService.getUserByEmail(clientEmail);

        String accessToken = jwtService.generateAccessToken(user);
        AuthenticationResponseDTO authenticationResponseDTO = AuthenticationResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponseDTO);
    }
}
