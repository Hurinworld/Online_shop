package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Shop;

import java.util.List;

public interface ShopService {
    List<Shop> getAllShops();

    Shop saveShop(Shop shop);

    Shop getShop(Long id);

    void deleteShop(Long id);
}
