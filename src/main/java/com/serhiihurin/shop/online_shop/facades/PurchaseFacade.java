package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.Purchase;

import java.util.List;

public interface PurchaseFacade {
    Purchase makePurchase(Long clientId, List<Long> productIds);

}
