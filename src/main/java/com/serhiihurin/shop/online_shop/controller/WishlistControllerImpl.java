package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.controller.interfaces.WishlistController;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.dto.WishlistResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.Wishlist;
import com.serhiihurin.shop.online_shop.facades.interfaces.WishlistFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/online-shop/wishlist")
@PreAuthorize("hasAnyRole('CLIENT', 'SHOP_OWNER')")
@Tag(name = "Wishlist")
@RequiredArgsConstructor
@Slf4j
public class WishlistControllerImpl implements WishlistController {
    private final WishlistFacade wishlistFacade;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('client view info', 'shop owner view info')")
    public WishlistResponseDTO getUserWishlist(User currentAuthenticatedUser) {
        List<Wishlist> wishlist = wishlistFacade.getUserWishlist(currentAuthenticatedUser.getId());

        return WishlistResponseDTO.builder()
                .userId(currentAuthenticatedUser.getId())
                .product(
                        wishlist.stream()
                        .map(item -> modelMapper.map(item.getProduct(), ProductResponseDTO.class))
                        .collect(Collectors.toList())
                )
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('client view info', 'shop owner view info')")
    public ResponseEntity<Void> addProductToWishlist(User currentAuthenticatedUser, @RequestParam Long productId) {
        wishlistFacade.addProductToWishlist(currentAuthenticatedUser,productId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('client view info', 'shop owner view info')")
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteProductFromWishlist(User currentAuthenticatedUser, @PathVariable Long productId) {
        wishlistFacade.deleteProductFromWishlist(currentAuthenticatedUser.getId(), productId);
        return ResponseEntity.ok().build();
    }
}
