package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.Wishlist;
import com.serhiihurin.shop.online_shop.services.ProductService;
import com.serhiihurin.shop.online_shop.services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WishlistFacadeImpl implements WishlistFacade{
    private final WishlistService wishlistService;
    private final ProductService productService;

    @Override
    public List<Wishlist> getUserWishlist(Long userId) {
        return wishlistService.getUserWishlist(userId);
    }

    @Override
    public void addProductToWishlist(User currentAuthenticatedUser, Long productId) {
        wishlistService.addProductToWishlist(currentAuthenticatedUser, productService.getProduct(productId));
    }

    @Override
    public void deleteProductFromWishlist(Long productId) {
        wishlistService.deleteProductFromWishlist(productId);
    }
}
