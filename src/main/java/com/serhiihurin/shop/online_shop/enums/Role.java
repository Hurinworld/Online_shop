package com.serhiihurin.shop.online_shop.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.serhiihurin.shop.online_shop.enums.Permission.*;

@RequiredArgsConstructor
public enum Role {
    CLIENT(
            Set.of(
                    CLIENT_READ,
                    CLIENT_CREATE,
                    CLIENT_UPDATE,
                    CLIENT_DELETE
            )
    ),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_CREATE,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    SHOP_OWNER_READ,
                    SHOP_OWNER_CREATE,
                    SHOP_OWNER_UPDATE,
                    SHOP_OWNER_DELETE
            )
    ),
    SUPER_ADMIN(Collections.emptySet()),
    SHOP_OWNER(
            Set.of(
                    SHOP_OWNER_READ,
                    SHOP_OWNER_CREATE,
                    SHOP_OWNER_UPDATE,
                    SHOP_OWNER_DELETE
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
