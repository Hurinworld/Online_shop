package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getProductsByNameContains(String productName);

    List<Product> getProductsByNameContainsAndPriceBetween(
            String productName, Double minimalPrice, Double maximalPrice
    );

    List<Product> getProductsByNameContainsAndPriceGreaterThan(String productName, Double price);

    List<Product> getProductsByNameContainsAndPriceLessThan(String productName, Double price);

    List<Product> getProductsByShopIdAndNameContains(Long shopId, String name);

    Optional<Product> getProductByName(String name);
}
