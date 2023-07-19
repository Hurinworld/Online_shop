package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ShopRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Shop;

public interface ShopFacade {
    Shop updateShop(ShopRequestDTO shopRequestDTO);
}
