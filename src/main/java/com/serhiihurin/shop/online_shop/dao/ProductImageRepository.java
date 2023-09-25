package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> getProductImagesByProductId(Long productId);
}
