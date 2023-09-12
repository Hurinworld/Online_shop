package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.Discount;
import com.serhiihurin.shop.online_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByProductId(Long productId);
}
