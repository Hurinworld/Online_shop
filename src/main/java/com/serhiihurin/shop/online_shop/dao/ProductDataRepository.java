package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.ProductData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDataRepository extends JpaRepository<ProductData, Long> {
    List<ProductData> getProductDataByShopId(Long id);
}
