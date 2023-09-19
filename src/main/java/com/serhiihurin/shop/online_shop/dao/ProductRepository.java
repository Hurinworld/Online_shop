package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //TODO dont use optional for list //done
    List<Product> getProductsByShopId(Long id);

    Optional<Product> getProductByName(String name);
}
