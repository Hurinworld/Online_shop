package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ProductImageRepository;
import com.serhiihurin.shop.online_shop.entity.ProductImage;
import com.serhiihurin.shop.online_shop.services.interfaces.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;

    @Override
    public List<ProductImage> getProductImagesByProductId(Long productId) {
        return productImageRepository.getProductImagesByProductId(productId);
    }

    @Override
    public void addProductImage(ProductImage productImage) {
        productImageRepository.save(productImage);
    }
}
