package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ShopRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.entity.User;

import java.util.List;

public interface ShopFacade {
    List<Shop> getAllShops();

    Shop getShop(Long id);

    Shop createShop(User currentAuthenticatedUser, ShopRequestDTO shopRequestDTO);

    Shop updateShop(ShopRequestDTO shopRequestDTO);

    void deleteShop(Long id);
}
