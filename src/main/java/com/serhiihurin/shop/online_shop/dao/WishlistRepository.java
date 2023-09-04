package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.Wishlist;
import com.serhiihurin.shop.online_shop.entity.composite_id.UserProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, UserProductId> {
    List<Wishlist> findAllByUserId(Long userId);

    void deleteByProductId(Long productId);
}
