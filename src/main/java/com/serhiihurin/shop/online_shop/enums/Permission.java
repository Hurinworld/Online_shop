package com.serhiihurin.shop.online_shop.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    SHOP_OWNER_READ("shop owner:read"),
    SHOP_OWNER_CREATE("shop owner:create"),
    SHOP_OWNER_UPDATE("shop owner:update"),
    SHOP_OWNER_DELETE("shop owner:delete");

    @Getter
    private final String permission;
}
