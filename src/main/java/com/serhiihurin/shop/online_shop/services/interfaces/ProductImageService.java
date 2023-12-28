package com.serhiihurin.shop.online_shop.services.interfaces;

import com.serhiihurin.shop.online_shop.entity.Image;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.ProductImage;

import java.util.List;

public interface ProductImageService {
    List<ProductImage> addProductImage(Product product, List<Image> images);

    List<ProductImage> getProductImagesByProductId(Long productId);

    ProductImage getProductImageByImageToken(String imageToken);

    void deleteProductImage(ProductImage productImage);
}
