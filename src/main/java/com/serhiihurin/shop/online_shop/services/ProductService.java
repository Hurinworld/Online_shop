package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.entity.Product;

import java.util.List;

public interface ProductService {
    List<Client> getAllProducts();

    void saveProduct(Product product);

    Client getProduct(Long id);

    void deleteProduct(Long id);
}
