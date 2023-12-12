package com.serhiihurin.shop.online_shop.facades.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileFacade {
    List<String> saveProductImages(Long productId, MultipartFile[] files);

    byte[] getProductImage(String imageToken);
}
