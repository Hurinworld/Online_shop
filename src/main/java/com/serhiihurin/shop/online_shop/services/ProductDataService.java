package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dto.ProductDataRequestDTO;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.entity.Shop;

import java.util.List;

public interface ProductDataService {
    List<ProductData> getAllProductData();

    List<ProductData> getProductDataByShopId(Long id);

    ProductData getProductData(Long id);

    ProductData saveProductData(ProductData productData);

    ProductData updateProductData(
            ProductDataRequestDTO productDataRequestDTO,
            Shop shop,
            ProductData productData
    );

    void deleteProductData(Long id);
}
