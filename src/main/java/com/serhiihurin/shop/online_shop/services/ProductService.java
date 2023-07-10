package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    List<Product> findProductsByProductDataId(Long id);

    Product getProduct(Long id);

    Product saveProduct(Product product);

    void deleteProduct(Long id);
}
