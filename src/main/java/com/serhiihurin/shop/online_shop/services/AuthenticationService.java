package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.request.AuthenticationRequestDTO;
import com.serhiihurin.shop.online_shop.request.RegisterRequestDTO;
import com.serhiihurin.shop.online_shop.dto.AuthenticationResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponseDTO register(RegisterRequestDTO request);

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);

    void updateAccessToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;
}
