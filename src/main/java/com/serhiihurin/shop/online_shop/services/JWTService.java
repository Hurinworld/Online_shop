package com.serhiihurin.shop.online_shop.services;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.serhiihurin.shop.online_shop.entity.Client;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public interface JWTService {
    String createAccessToken(Client client);

    String createRefreshToken(Client client);

    String decodeAndVerifyToken(String token);

    DecodedJWT decodeJWT(String authorizationHeader);

    ArrayList<SimpleGrantedAuthority> getRoles (DecodedJWT decodedJWT);

    String extractUsername(String token);

    String generateAccessToken(Map<String, Object> extraClaims,
                               UserDetails userDetails);
    String generateAccessToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    Date extractExpiration(String token);
}
