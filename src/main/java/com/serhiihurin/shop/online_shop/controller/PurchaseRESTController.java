package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Purchase;
import com.serhiihurin.shop.online_shop.services.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online_shop/purchases")
public class PurchaseRESTController {
    @Autowired
    private PurchaseService purchaseService;

    @GetMapping
    public List<Purchase> getAllPurchases() {
        return purchaseService.getAllPurchases();
    }

    @GetMapping
    public List<Purchase> getPurchasesByClientId(@RequestParam Long clientId) {
        return purchaseService.getPurchasesByClientId(clientId);
    }

    @GetMapping("/{id}")
    public Purchase getPurchase(@PathVariable Long id) {
        return purchaseService.getPurchase(id);
    }

    @PostMapping
    public Purchase addNewPurchase(@RequestBody Purchase purchase) {
        purchaseService.savePurchase(purchase);
        return purchase;
    }

    @PutMapping
    public Purchase updatePurchase(@RequestBody Purchase purchase) {
        purchaseService.savePurchase(purchase);
        return purchase;
    }

    @DeleteMapping("/{id}")
    public String deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return "Client with id = " + id + " was deleted";
    }
}
