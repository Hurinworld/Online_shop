package com.serhiihurin.shop.online_shop.services;

public interface ImageTokenService {
    String createImageToken(String filepath);
    String getPathByImageToken(String imageToken);
}
