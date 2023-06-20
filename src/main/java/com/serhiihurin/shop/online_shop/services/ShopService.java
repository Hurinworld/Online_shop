package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.entity.Shop;

import java.util.List;

public interface ShopService {
    List<Client> getAllShops();

    void saveShop(Shop shop);

    Client getShop(Long id);

    void deleteShop(Long id);
}
