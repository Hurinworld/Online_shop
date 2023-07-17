package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ShopDTO;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.services.ShopService;
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
@RequiredArgsConstructor
public class ShopRESTController {
    private final ShopService shopService;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('admin view info')")
    public List<ShopDTO> getAllShops() {
        return modelMapper.map(
                shopService.getAllShops(),
                new TypeToken<List<ShopDTO>>(){}.getType()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin view info')")
    public ShopDTO getShop(@PathVariable Long id) {
        return modelMapper.map(
                shopService.getShop(id),
                ShopDTO.class
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('shop management')")
    public ResponseEntity<ShopDTO> addNewShop(@RequestBody Shop shop) {
        return ResponseEntity.ok(
                modelMapper.map(
                        shopService.saveShop(shop),
                        ShopDTO.class
                )
        );
    }

    @PutMapping
    @PreAuthorize("hasAuthority('shop management')")
    public ResponseEntity<Shop> updateShop(@RequestBody Shop shop) {
        return ResponseEntity.ok(shopService.saveShop(shop));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('shop management', 'super admin info deletion')")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ResponseEntity.ok().build();
    }
}
