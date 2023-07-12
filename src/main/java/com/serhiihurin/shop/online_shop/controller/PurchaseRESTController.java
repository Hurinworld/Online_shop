package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Purchase;
import com.serhiihurin.shop.online_shop.facades.PurchaseFacadeImpl;
import com.serhiihurin.shop.online_shop.services.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/purchases")
@PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
@RequiredArgsConstructor
public class PurchaseRESTController {
    private final PurchaseService purchaseService;
    private final PurchaseFacadeImpl purchaseFacade;

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public List<Purchase> getAllPurchases() {
        return purchaseService.getAllPurchases();
    }

    @GetMapping("/client")
    @PreAuthorize("hasAnyAuthority('client:read', 'admin:read')")
    public List<Purchase> getPurchasesByClientId(@RequestParam Long clientId) {
        return purchaseService.getPurchasesByClientId(clientId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('client:read', 'admin:read')")
    public Purchase getPurchase(@PathVariable Long id) {
        return purchaseService.getPurchase(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('client:create')")
    public Purchase makePurchase(@RequestParam Long clientId,
                                 @RequestParam(name = "productId") List<Long> productIds) {
        return purchaseFacade.makePurchase(clientId, productIds);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<Purchase> updatePurchase(@RequestBody Purchase purchase) {
        return ResponseEntity.ok(purchaseService.savePurchase(purchase));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.ok().build();
    }
}
