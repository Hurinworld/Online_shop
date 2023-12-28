package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ProductImageRepository;
import com.serhiihurin.shop.online_shop.entity.Image;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.ProductImage;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.services.interfaces.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;

    @Override
    public List<ProductImage> addProductImage(Product product, List<Image> images) {
        List<ProductImage> productImages = new ArrayList<>();
        for (Image image : images) {
            productImages.add(
                    productImageRepository.save(
                            ProductImage.builder()
                                    .product(product)
                                    .imageInfo(image)
                                    .build()
                    )
            );
        }
        return productImages;
    }

    @Override
    public List<ProductImage> getProductImagesByProductId(Long productId) {
        return productImageRepository.getProductImagesByProductId(productId);
    }

    @Override
    public ProductImage getProductImageByImageToken(String imageToken) {
        return productImageRepository.getProductImageByImageToken(imageToken)
                .orElseThrow(() -> new ApiRequestException("Could not find image by specified token"));
    }

    @Override
    public void deleteProductImage(ProductImage productImage) {
        productImageRepository.delete(productImage);
    }
}
