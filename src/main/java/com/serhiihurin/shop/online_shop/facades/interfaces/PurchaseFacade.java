package com.serhiihurin.shop.online_shop.facades.interfaces;

import com.serhiihurin.shop.online_shop.dto.PurchaseRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Purchase;
import com.serhiihurin.shop.online_shop.entity.User;

import java.util.List;

public interface PurchaseFacade {
    List<Purchase> getAllPurchases();

    List<Purchase> getPurchasesByClientId(Long clientId);

    Purchase getPurchase(Long id);

    Purchase makePurchase(User currentAuthenticatedUser, PurchaseRequestDTO purchaseRequestDTO);

    void deletePurchase(Long id);
}
