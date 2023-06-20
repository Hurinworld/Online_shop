package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase,Long> {
    List<Purchase> getPurchasesByClient(Client client);
}
