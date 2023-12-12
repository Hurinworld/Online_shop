package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ProductImageRepository;
import com.serhiihurin.shop.online_shop.services.interfaces.ImageTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageTokenServiceImpl implements ImageTokenService {
    private final ProductImageRepository productImageRepository;

    @Override
    public String createImageToken(String filepath) {
        return UUID.nameUUIDFromBytes(filepath.getBytes()).toString();
    }

    @Override
    public String getPathByImageToken(String imageToken) {
        return productImageRepository.getProductImageByImageToken(imageToken).getFilepath();
    }
}
