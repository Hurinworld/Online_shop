package com.serhiihurin.shop.online_shop.services.interfaces;

import com.serhiihurin.shop.online_shop.entity.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<ProductImage> saveProductImages(Long productId, MultipartFile[] files);

    byte[] getProductImage(String filepath);
}
