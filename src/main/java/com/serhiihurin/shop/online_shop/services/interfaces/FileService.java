package com.serhiihurin.shop.online_shop.services.interfaces;

import com.serhiihurin.shop.online_shop.entity.ProductImage;
import com.serhiihurin.shop.online_shop.entity.UserImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<ProductImage> saveProductImages(Long productId, MultipartFile[] files);

    List<UserImage> saveUserImages(Long userId, MultipartFile[] files);

    byte[] getImage(String filepath);
}
