package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.services.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/shops")
@PreAuthorize("hasAnyRole('SHOP_OWNER', 'ADMIN')")
@RequiredArgsConstructor
public class ShopRESTController {
    private final ShopService shopService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public List<Shop> getAllShops() {
        return shopService.getAllShops();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public Shop getShop(@PathVariable Long id) {
        return shopService.getShop(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('shop owner:read')")
    public ResponseEntity<Shop> addNewShop(@RequestBody Shop shop) {
        return ResponseEntity.ok(shopService.saveShop(shop));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('shop owner:update')")
    public ResponseEntity<Shop> updateShop(@RequestBody Shop shop) {
        return ResponseEntity.ok(shopService.saveShop(shop));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('shop owner:delete', 'admin:delete')")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ResponseEntity.ok().build();
    }
}
