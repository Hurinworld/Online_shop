package com.serhiihurin.shop.online_shop.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.serhiihurin.shop.online_shop.enums.Permission.*;

@RequiredArgsConstructor
public enum Role {
    CLIENT(
            Set.of(
                    CLIENT_VIEW_INFO,
                    ACCOUNT_CREATION,
                    ACCOUNT_MANAGEMENT,
                    FEEDBACK_MANAGEMENT,
                    PURCHASE_CREATION
            )
    ),
    ADMIN(
            Set.of(
                    ADMIN_VIEW_INFO,
                    PURCHASE_MANAGEMENT,
                    ADMIN_INFO_DELETION,
                    SERVICE_INFORMATION_MANAGEMENT
            )
    ),
    SUPER_ADMIN(
            Set.of(
                    ADMIN_VIEW_INFO,
                    ACCOUNT_CREATION,
                    ROLE_MANAGEMENT,
                    PURCHASE_MANAGEMENT,
                    SERVICE_INFORMATION_MANAGEMENT,
                    ADMIN_INFO_DELETION,
                    SUPER_ADMIN_INFO_DELETION
            )
    ),
    SHOP_OWNER(
            Set.of(
                    SHOP_OWNER_VIEW_INFO,
                    ACCOUNT_MANAGEMENT,
                    SHOP_MANAGEMENT,
                    PRODUCT_MANAGEMENT
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
