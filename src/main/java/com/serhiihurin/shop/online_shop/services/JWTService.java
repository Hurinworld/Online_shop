package com.serhiihurin.shop.online_shop.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

public interface JWTService {
    String extractUsername(String token);

    String generateAccessToken(Map<String, Object> extraClaims,
                               UserDetails userDetails);

    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    Date extractExpiration(String token);
}
