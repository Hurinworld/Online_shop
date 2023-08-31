package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.AuthenticationRequestDTO;
import com.serhiihurin.shop.online_shop.dto.AuthenticationResponseDTO;
import com.serhiihurin.shop.online_shop.dto.RegisterRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationFacade {
    AuthenticationResponseDTO register(RegisterRequestDTO registerRequestDTO);

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);

    void updateAccessToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;
}
