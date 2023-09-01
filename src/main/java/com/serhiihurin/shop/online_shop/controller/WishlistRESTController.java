package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.dto.WishlistResponseDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.Wishlist;
import com.serhiihurin.shop.online_shop.services.WishlistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/online-shop/wishlist")
@PreAuthorize("hasAnyRole('CLIENT', 'SHOP_OWNER')")
@Tag(name = "Wishlist")
@RequiredArgsConstructor
@Slf4j
public class WishlistRESTController {
    private final WishlistService wishlistService;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('client view info', 'shop owner view info')")
    public WishlistResponseDTO getUserWishlist(User currentAuthenticatedUser) {
        List<Wishlist> wishlist = wishlistService.getUserWishlist(currentAuthenticatedUser.getId());
        List<Product> wishlistProducts = new ArrayList<>();
        for (Wishlist item : wishlist) {
            wishlistProducts.add(item.getProduct());
        }

        return WishlistResponseDTO.builder()
                .userId(currentAuthenticatedUser.getId())
                .product(
                        modelMapper.map(
                                wishlistProducts,
                                new TypeToken<List<ProductResponseDTO>>(){}.getType()
                        )
                )
                .build();
    }
}
