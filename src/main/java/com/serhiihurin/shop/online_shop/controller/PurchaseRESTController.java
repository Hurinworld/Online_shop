package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.PurchaseDTO;
import com.serhiihurin.shop.online_shop.facades.PurchaseFacadeImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PurchaseRESTController {
    private final PurchaseFacadeImpl purchaseFacade;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('admin view info')")
    public List<PurchaseDTO> getAllPurchases() {
        log.info("Admin: getting list of purchases");
        return modelMapper.map(
                purchaseFacade.getAllPurchases(),
                new TypeToken<List<PurchaseDTO>>(){}.getType()
        );
    }

    @GetMapping("/client-purchases")
    @PreAuthorize("hasAnyAuthority('client view info', 'admin view info')")
    public List<PurchaseDTO> getPurchasesByClientId(@RequestParam Long clientId) {
        return modelMapper.map(
                purchaseFacade.getPurchasesByClientId(clientId),
                new TypeToken<List<PurchaseDTO>>(){}.getType()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('client view info', 'admin view info')")
    public PurchaseDTO getPurchase(@PathVariable Long id) {
        return modelMapper.map(
                purchaseFacade.getPurchase(id),
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('purchase management')")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        purchaseFacade.deletePurchase(id);
        log.info("Deleting of purchase with id: {}", id);
        return ResponseEntity.ok().build();
    }
}
