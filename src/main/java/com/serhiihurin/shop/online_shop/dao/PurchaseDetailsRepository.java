package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.PurchaseDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseDetailsRepository extends JpaRepository<PurchaseDetails, Long> {
}
