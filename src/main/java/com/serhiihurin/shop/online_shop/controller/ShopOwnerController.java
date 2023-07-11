package com.serhiihurin.shop.online_shop.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shop-owner")
public class ShopOwnerController {

    @GetMapping
    public String get() {
        return "GET:: shop owner controller";
    }

    @PostMapping
    public String post() {
        return "POST:: shop owner controller";
    }

    @PutMapping
    public String put() {
        return "PUT:: shop owner controller";
    }

    @DeleteMapping
    public String delete() {
        return "DELETE:: shop owner controller";
    }
}
