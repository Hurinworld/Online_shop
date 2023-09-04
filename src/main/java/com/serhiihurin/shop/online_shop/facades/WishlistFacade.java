package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.Wishlist;

import java.util.List;

public interface WishlistFacade {
    List<Wishlist> getUserWishlist(Long userId);

    void addProductToWishlist(User currentAuthenticatedUser, Long productId);

    void deleteProductFromWishlist(Long productId);
}
