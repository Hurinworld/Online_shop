package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dto.ShopRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.entity.User;

import java.util.List;

public interface ShopService {
    List<Shop> getAllShops();

    Shop createShop(User currentAuthenticatedUser, ShopRequestDTO shopRequestDTO);

    Shop getShop(Long id);

    Shop getShopByOwnerId(Long ownerId);

    Shop updateShop(ShopRequestDTO shopRequestDTO, Shop shop);

    void deleteShop(Long id);
}
