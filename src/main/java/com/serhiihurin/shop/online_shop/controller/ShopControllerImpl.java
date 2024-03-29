package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.controller.interfaces.ShopController;
import com.serhiihurin.shop.online_shop.dto.ShopRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ShopResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.facades.interfaces.ShopFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/shops")
@PreAuthorize("hasAnyRole('SHOP_OWNER', 'ADMIN', 'SUPER_ADMIN')")
@Tag(name = "Shop")
@RequiredArgsConstructor
public class ShopControllerImpl implements ShopController {
    private final ShopFacade shopFacade;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('admin view info')")
    public List<ShopResponseDTO> getAllShops() {
        return modelMapper.map(
                shopFacade.getAllShops(),
                new TypeToken<List<ShopResponseDTO>>() {
                }.getType()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin view info')")
    public ShopResponseDTO getShop(@PathVariable Long id) {
        return modelMapper.map(
                shopFacade.getShop(id),
                ShopResponseDTO.class
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('shop management')")
    public ResponseEntity<ShopResponseDTO> addNewShop(
            User currentAuthenticatedUser,
            @RequestBody ShopRequestDTO shopRequestDTO
    ) {
        return ResponseEntity.ok(
                modelMapper.map(
                        shopFacade.createShop(currentAuthenticatedUser, shopRequestDTO),
                        ShopResponseDTO.class
                )
        );
    }

    @PutMapping
    @PreAuthorize("hasAuthority('shop management')")
    public ResponseEntity<ShopResponseDTO> updateShop(@RequestBody ShopRequestDTO shopRequestDTO) {
        return ResponseEntity.ok(
                modelMapper.map(
                        shopFacade.updateShop(shopRequestDTO),
                        ShopResponseDTO.class
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('shop management', 'super admin info deletion')")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopFacade.deleteShop(id);
        return ResponseEntity.ok().build();
    }
}
