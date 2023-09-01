package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Wishlist;

import java.util.List;

public interface WishlistService {
    List<Wishlist> getUserWishlist(Long userId);
}
