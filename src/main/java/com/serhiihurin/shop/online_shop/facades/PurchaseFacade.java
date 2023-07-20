package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.Purchase;

import java.util.List;

public interface PurchaseFacade {
    List<Purchase> getAllPurchases();

    List<Purchase> getPurchasesByClientId(Long clientId);

    Purchase getPurchase(Long id);

    Purchase makePurchase(Long clientId, List<Long> productIds);

    void deletePurchase(Long id);
}
