package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.entity.Purchase;

import java.util.List;

public interface PurchaseService {
    List<Client> getAllPurchases();

    List<Purchase> getPurchasesByClient(Client client);

    void savePurchase(Purchase purchase);

    Client getPurchase(Long id);

    void deletePurchase(Long id);
}
