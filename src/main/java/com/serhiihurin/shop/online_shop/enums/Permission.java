package com.serhiihurin.shop.online_shop.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    // TODO: 14.07.2023 permissions should not depend on roles //done
    CLIENT_VIEW_INFO ("client view info"),
    SHOP_OWNER_VIEW_INFO ("shop owner view info"),
    ADMIN_VIEW_INFO ("admin view info"),
    ACCOUNT_CREATION ("account creation"),
    ACCOUNT_MANAGEMENT("account management"),
    ROLE_MANAGEMENT ("role management"),
    SHOP_MANAGEMENT("shop management"),
    PRODUCT_DATA_MANAGEMENT("product data management"),
    PRODUCT_MANAGEMENT("product management"),
    FEEDBACK_MANAGEMENT("feedback management"),
    PURCHASE_CREATION("purchase creation"),
    PURCHASE_MANAGEMENT("purchase management"),
    ADMIN_INFO_DELETION("admin info deletion"),
    SERVICE_INFORMATION_MANAGEMENT("service information management"),
    SUPER_ADMIN_INFO_DELETION("super admin info deletion");

    @Getter
    private final String permission;
}
