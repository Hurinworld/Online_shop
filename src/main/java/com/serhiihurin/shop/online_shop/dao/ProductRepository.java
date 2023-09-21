package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //TODO dont use optional for list //done
    List<Product> getProductsByShopId(Long id);

    List<Product> getProductsByNameContains(String productName);

    List<Product> getProductsByNameContainsAndPriceBetween(
            String productName, Double minimalPrice, Double maximalPrice
    );

    List<Product> getProductsByNameContainsAndPriceBetweenOrderByPriceAsc(
            String productName, Double minimalPrice, Double maximalPrice
    );

    List<Product> getProductsByNameContainsAndPriceBetweenOrderByPriceDesc(
            String productName, Double minimalPrice, Double maximalPrice
    );

//    List<Product> getProductsByPriceLessThanOrderByPriceAsc(Double price);

    List<Product> getProductsByNameContainsAndPriceLessThanOrderByPriceAsc(String productName, Double price);

    List<Product> getProductsByNameContainsAndPriceLessThanOrderByPriceDesc(String productName, Double price);

    List<Product> getProductsByNameContainsAndPriceGreaterThanOrderByPriceAsc(String productName, Double price);

    List<Product> getProductsByNameContainsAndPriceGreaterThanOrderByPriceDesc(String productName, Double price);

    List<Product> getProductsByNameContainsAndPriceGreaterThan(String productName, Double price);

    List<Product> getProductsByNameContainsAndPriceLessThan(String productName, Double price);

//    List<Product> getProductsByPriceLessThanOrderByPriceDesc(Double price);

    List<Product> getProductsByNameContainsOrderByPriceAsc(String productName);

    List<Product> getProductsByNameContainsOrderByPriceDesc(String productName);

    Optional<Product> getProductByName(String name);
}
