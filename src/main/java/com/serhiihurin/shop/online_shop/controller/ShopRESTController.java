package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online_shop/shops")
public class ShopRESTController {
    @Autowired
    private ShopService shopService;

    @GetMapping
    public List<Shop> getAllShops() {
        return shopService.getAllShops();
    }

    @GetMapping("/{id}")
    public Shop getShop(@PathVariable Long id) {
        return shopService.getShop(id);
    }

    @PostMapping
    public Shop addNewShop(@RequestBody Shop shop) {
        shopService.saveShop(shop);
        return shop;
    }

    @PutMapping
    public Shop updateShop(@RequestBody Shop shop) {
        shopService.saveShop(shop);
        return shop;
    }

    @DeleteMapping("/{id}")
    public String deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return "Shop with id = " + id + " was deleted";
    }
}
