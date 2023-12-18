package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> getProductImagesByProductId(Long productId);

    @Query("SELECT productImage FROM ProductImage productImage JOIN productImage.imageInfo " +
            "WHERE productImage.imageInfo.imageToken = :imageToken")
    Optional<ProductImage> getProductImageByImageToken(@Param("imageToken") String imageToken);
}
