package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.services.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/shops")
@RequiredArgsConstructor
public class ShopRESTController {
    private final ShopService shopService;

    @GetMapping
    public List<Shop> getAllShops() {
        return shopService.getAllShops();
    }

    @GetMapping("/{id}")
    public Shop getShop(@PathVariable Long id) {
        return shopService.getShop(id);
    }

    @PostMapping
    public ResponseEntity<Shop> addNewShop(@RequestBody Shop shop) {
        return ResponseEntity.ok(shopService.saveShop(shop));
    }

    @PutMapping
    public ResponseEntity<Shop> updateShop(@RequestBody Shop shop) {
        return ResponseEntity.ok(shopService.saveShop(shop));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ResponseEntity.ok().build();
    }
}
