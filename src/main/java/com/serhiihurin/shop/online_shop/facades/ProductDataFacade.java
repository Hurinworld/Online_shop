package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.ProductData;

import java.util.List;

public interface ProductDataFacade {
    List<ProductData> getAllProductData();

    List<ProductData> getAllProductDataByShopId(Long id);

    ProductData getProductData(Long id);

    ProductData addProductData(Long shopId, ProductData productData);

    ProductData updateProductData(ProductData productData);

    void deleteProductData(Long id);
}
