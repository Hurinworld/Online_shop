package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.controller.interfaces.PurchaseController;
import com.serhiihurin.shop.online_shop.dto.PurchaseAdminResponseDTO;
import com.serhiihurin.shop.online_shop.dto.PurchaseRequestDTO;
import com.serhiihurin.shop.online_shop.dto.PurchaseResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.facades.PurchaseFacadeImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Purchase")
@RequiredArgsConstructor
public class PurchaseControllerImpl implements PurchaseController {
    private final PurchaseFacadeImpl purchaseFacade;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('admin view info')")
    public List<PurchaseAdminResponseDTO> getAllPurchases() {
        return modelMapper.map(
                purchaseFacade.getAllPurchases(),
                new TypeToken<List<PurchaseAdminResponseDTO>>() {
                }.getType()
        );
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyAuthority('client view info', 'admin view info')")
    public List<PurchaseAdminResponseDTO> getPurchasesByClientId(@PathVariable Long clientId) {
        return modelMapper.map(
                purchaseFacade.getPurchasesByClientId(clientId),
                new TypeToken<List<PurchaseAdminResponseDTO>>() {
                }.getType()
        );
    }

    @GetMapping("/client/me")
    @PreAuthorize("hasAnyAuthority('client view info', 'admin view info')")
    public List<PurchaseResponseDTO> getPurchasesByClientId(User currentAuthenticatedUser) {
        return modelMapper.map(
                purchaseFacade.getPurchasesByClientId(currentAuthenticatedUser.getId()),
                new TypeToken<List<PurchaseResponseDTO>>() {
                }.getType()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin view info')")
    public PurchaseAdminResponseDTO getPurchase(@PathVariable Long id) {
        return modelMapper.map(
                purchaseFacade.getPurchase(id),
                PurchaseAdminResponseDTO.class
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('purchase creation')")
    public PurchaseResponseDTO makePurchase(
            User currentAuthenticatedUser,
            @RequestBody PurchaseRequestDTO purchaseRequestDTO
    ) {
        return modelMapper.map(
                purchaseFacade.makePurchase(currentAuthenticatedUser, purchaseRequestDTO),
                PurchaseResponseDTO.class
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('purchase management')")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        purchaseFacade.deletePurchase(id);
        return ResponseEntity.ok().build();
    }
}
