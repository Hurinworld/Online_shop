package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.PurchaseDTO;
import com.serhiihurin.shop.online_shop.entity.Purchase;
import com.serhiihurin.shop.online_shop.facades.PurchaseFacadeImpl;
import com.serhiihurin.shop.online_shop.services.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/purchases")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'CLIENT')")
@RequiredArgsConstructor
public class PurchaseRESTController {
    private final PurchaseService purchaseService;
    private final PurchaseFacadeImpl purchaseFacade;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('admin view info')")
    public List<PurchaseDTO> getAllPurchases() {
        return modelMapper.map(
                purchaseService.getAllPurchases(),
                new TypeToken<List<PurchaseDTO>>(){}.getType()
        );
    }

    @GetMapping("/client")
    @PreAuthorize("hasAnyAuthority('client view info', 'admin view info')")
    public List<PurchaseDTO> getPurchasesByClientId(@RequestParam Long clientId) {
        return modelMapper.map(
                purchaseService.getPurchasesByClientId(clientId),
                new TypeToken<List<PurchaseDTO>>(){}.getType()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('client view info', 'admin view info')")
    public PurchaseDTO getPurchase(@PathVariable Long id) {
        return modelMapper.map(
                purchaseService.getPurchase(id),
                PurchaseDTO.class
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('purchase creation')")
    public PurchaseDTO makePurchase(@RequestParam Long clientId,
                                 @RequestParam(name = "productId") List<Long> productIds) {
        return modelMapper.map(
                purchaseFacade.makePurchase(clientId, productIds),
                PurchaseDTO.class
        );
    }

    @PutMapping
    @PreAuthorize("hasAuthority('purchase management')")
    public ResponseEntity<Purchase> updatePurchase(@RequestBody Purchase purchase) {
        return ResponseEntity.ok(purchaseService.savePurchase(purchase));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('purchase management')")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.ok().build();
    }
}
