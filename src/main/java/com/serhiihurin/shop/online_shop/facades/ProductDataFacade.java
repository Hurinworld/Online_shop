package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.ProductData;

import java.util.List;

public interface ProductDataFacade {
    List<ProductData> showAllProductData();
    List<ProductData> showAllProductDataByShopId(Long id);
    ProductData showProductData(Long id);
    ProductData addProductData(Long shopId, ProductData productData);
    ProductData updateProductData(ProductData productData);
    String deleteProductData(Long id);
}
