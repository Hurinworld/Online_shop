package com.serhiihurin.shop.online_shop.facades.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileFacade {
    List<String> saveProductImages(Long userId, Long productId, MultipartFile[] files);

    List<String> saveUserImages(Long userId, MultipartFile[] files);

    void deleteUserImage(String imageToken, Long userId);

    void deleteProductImage(String imageToken, Long userId);

    byte[] getImage(String imageToken);
}
