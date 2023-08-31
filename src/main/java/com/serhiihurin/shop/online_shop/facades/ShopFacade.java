package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ShopRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Shop;

import java.util.List;

public interface ShopFacade {
    List<Shop> getAllShops();

    Shop getShop(Long id);

    Shop saveShop(ShopRequestDTO shopRequestDTO);

    Shop updateShop(ShopRequestDTO shopRequestDTO);

    void deleteShop(Long id);
}
