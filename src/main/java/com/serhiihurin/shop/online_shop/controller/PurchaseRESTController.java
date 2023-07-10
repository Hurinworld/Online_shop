package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Purchase;
import com.serhiihurin.shop.online_shop.facades.PurchaseFacadeImpl;
import com.serhiihurin.shop.online_shop.services.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/purchases")
@RequiredArgsConstructor
public class PurchaseRESTController {
    private final PurchaseService purchaseService;
    private final PurchaseFacadeImpl purchaseFacade;

    @GetMapping
    public List<Purchase> getAllPurchases() {
        return purchaseService.getAllPurchases();
    }

    @GetMapping("/client")
    public List<Purchase> getPurchasesByClientId(@RequestParam Long clientId) {
        return purchaseService.getPurchasesByClientId(clientId);
    }

    @GetMapping("/{id}")
    public Purchase getPurchase(@PathVariable Long id) {
        return purchaseService.getPurchase(id);
    }

    @PostMapping
    public Purchase makePurchase(@RequestParam Long clientId,
                                 @RequestParam(name = "productId") List<Long> productIds) {
        return purchaseFacade.makePurchase(clientId, productIds);
    }

    @PutMapping
    public Purchase updatePurchase(@RequestBody Purchase purchase) {
        purchaseService.savePurchase(purchase);
        return purchase;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.ok().build();
    }
}
