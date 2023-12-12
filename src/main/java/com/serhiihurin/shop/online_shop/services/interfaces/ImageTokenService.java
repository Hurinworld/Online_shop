package com.serhiihurin.shop.online_shop.services.interfaces;

public interface ImageTokenService {
    String createImageToken(String filepath);
    String getPathByImageToken(String imageToken);
}
