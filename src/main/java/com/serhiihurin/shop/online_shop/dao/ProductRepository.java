package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> getProductByName(String name);

    List<Product> getProductsByShopId(Long id);

    List<Product> getProductsByNameContains(String productName);

    List<Product> getProductsByNameContainsAndPriceBetween(
            String productName, Double minimalPrice, Double maximalPrice
    );

    List<Product> getProductsByNameContainsAndPriceGreaterThan(String productName, Double price);

    List<Product> getProductsByNameContainsAndPriceLessThan(String productName, Double price);

    List<Product> getProductsByNameContainsAndShopId(String productName, Long shopId);

    List<Product> getProductsByNameContainsAndShopIdAndPriceBetween(
            String productName, Long shopId, Double minimalPrice, Double maximalPrice
    );

    List<Product> getProductsByNameContainsAndShopIdAndPriceGreaterThan(String productName, Long shopId, Double price);

    List<Product> getProductsByNameContainsAndShopIdAndPriceLessThan(String productName, Long shopId, Double price);

    List<Product> getProductsByPriceBetween(Double minimalPrice, Double maximalPrice);

    List<Product> getProductsByPriceGreaterThan(Double price);

    List<Product> getProductsByPriceLessThan(Double price);

    List<Product> getProductsByShopIdAndPriceBetween(Long shopId, Double minimalPrice, Double maximalPrice);

    List<Product> getProductsByShopIdAndPriceGreaterThan(Long shopId, Double price);

    List<Product> getProductsByShopIdAndPriceLessThan(Long shopId, Double price);
}
