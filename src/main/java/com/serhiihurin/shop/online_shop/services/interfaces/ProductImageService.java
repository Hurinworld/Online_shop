package com.serhiihurin.shop.online_shop.services.interfaces;

import com.serhiihurin.shop.online_shop.entity.ProductImage;

import java.util.List;

public interface ProductImageService {

    List<ProductImage> getProductImagesByProductId(Long productId);
    void addProductImage(ProductImage productImage);
}
