package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Purchase;

import java.util.List;

public interface PurchaseService {
    List<Purchase> getAllPurchases();

    List<Purchase> getPurchasesByClientId(Long id);

    void savePurchase(Purchase purchase);

    Purchase getPurchase(Long id);

    void deletePurchase(Long id);
}
