package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.WishlistRepository;
import com.serhiihurin.shop.online_shop.entity.Wishlist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService{
    private final WishlistRepository wishlistRepository;

    @Override
    public List<Wishlist> getUserWishlist(Long userId) {
        return wishlistRepository.findAllByUserId(userId);
    }
}
