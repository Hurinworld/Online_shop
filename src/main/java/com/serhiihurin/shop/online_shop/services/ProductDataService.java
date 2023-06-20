package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.entity.Shop;

import java.util.List;

public interface ProductDataService {
    List<Client> getAllProductData();

    List<ProductData> getProductDataByShop(Shop shop);

    void saveProductData(ProductData productData);

    Client getProductData(Long id);

    void deleteProductData(Long id);
}
