package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> getProductByName(String name);

    List<Product> getProductsByShopId(Long id);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%'))")
    List<Product> getProductsByNameContains(@Param("productName") String productName);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.price BETWEEN :minimalPrice AND :maximalPrice")
    List<Product> getProductsByNameContainsAndPriceBetween(
            @Param("productName") String productName,
            @Param("minimalPrice") Double minimalPrice,
            @Param("maximalPrice") Double maximalPrice
    );

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.price > :price")
    List<Product> getProductsByNameContainsAndPriceGreaterThan(
            @Param("productName") String productName,
            @Param("price") Double price
    );

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.price < :price")
    List<Product> getProductsByNameContainsAndPriceLessThan(
            @Param("productName") String productName,
            @Param("price") Double price
    );

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.shop.id = :shopId")
    List<Product> getProductsByNameContainsAndShopId(
            @Param("productName") String productName,
            @Param("shopId") Long shopId
    );

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.shop.id = :shopId AND p.price BETWEEN :minimalPrice AND :maximalPrice")
    List<Product> getProductsByNameContainsAndShopIdAndPriceBetween(
            @Param("productName") String productName,
            @Param("shopId") Long shopId,
            @Param("minimalPrice") Double minimalPrice,
            @Param("maximalPrice") Double maximalPrice
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
