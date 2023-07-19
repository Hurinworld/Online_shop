package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ProductDataRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductDataResponseDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.ProductData;

import java.util.List;

public interface ProductDataFacade {
    List<ProductData> getAllProductData();

    List<ProductData> getAllProductDataByShopId(Long id);

    List<Product> getProductsByProductDataId(Long productDataId);

    ProductData getProductData(Long id);

    ProductData addProductData(Long shopId, ProductDataRequestDTO productDataRequestDTO);

    ProductDataResponseDTO updateProductData(ProductDataRequestDTO productDataRequestDTO);

    void deleteProductData(Long id);
}
