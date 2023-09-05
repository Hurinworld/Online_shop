package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.WishlistRepository;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.Wishlist;
import com.serhiihurin.shop.online_shop.entity.composite_id.UserProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService{
    private final WishlistRepository wishlistRepository;

    @Override
    public List<Wishlist> getWishlistsByProductsOnSale() {
        return wishlistRepository.getWishlistsByProductOnSale(true);
    }

    @Override
    public List<Wishlist> getUserWishlist(Long userId) {
        return wishlistRepository.findAllByUserId(userId);
    }

    @Override
    public void addProductToWishlist(User currentAuthenticatedUser, Product product) {
        wishlistRepository.save(
                Wishlist
                        .builder()
                        .id(
                                UserProductId
                                        .builder()
                                        .userId(currentAuthenticatedUser.getId())
                                        .productId(product.getId()).build()
                        )
                        .user(currentAuthenticatedUser)
                        .product(product).build()
        );
    }

    @Override
    public void deleteProductFromWishlist(Long productId) {
        wishlistRepository.deleteByProductId(productId);
    }
}
