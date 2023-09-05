package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.Wishlist;

import java.util.List;

public interface WishlistService {
    List<Wishlist> getWishlistsByProductsOnSale();

    List<Wishlist> getUserWishlist(Long userId);

    void addProductToWishlist(User currentAuthenticatedUser, Product product);

    void deleteProductFromWishlist(Long productId);
}
