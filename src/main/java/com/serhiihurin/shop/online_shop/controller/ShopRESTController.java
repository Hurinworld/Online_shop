package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ShopRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ShopResponseDTO;
import com.serhiihurin.shop.online_shop.facades.ShopFacade;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/shops")
@PreAuthorize("hasAnyRole('SHOP_OWNER', 'ADMIN', 'SUPER_ADMIN')")
@RequiredArgsConstructor
public class ShopRESTController {
    private final Logger logger;
    private final ShopFacade shopFacade;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('admin view info')")
    public List<ShopResponseDTO> getAllShops() {
        return modelMapper.map(
                shopFacade.getAllShops(),
                new TypeToken<List<ShopResponseDTO>>(){}.getType()
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
    public ResponseEntity<ShopResponseDTO> addNewShop(@RequestBody ShopRequestDTO shopRequestDTO) {
        logger.info("Adding new shop with name: {}", shopRequestDTO.getName());
        return ResponseEntity.ok(
                modelMapper.map(
                        shopFacade.saveShop(shopRequestDTO),
                        ShopResponseDTO.class
                )
        );
    }

    @PutMapping
    @PreAuthorize("hasAuthority('shop management')")
    public ResponseEntity<ShopResponseDTO> updateShop(@RequestBody ShopRequestDTO shopRequestDTO) {
        logger.info("Updating shop with id: {}", shopRequestDTO.getId());
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
        logger.info("Deleting shop with id: {}", id);
        return ResponseEntity.ok().build();
    }
}
