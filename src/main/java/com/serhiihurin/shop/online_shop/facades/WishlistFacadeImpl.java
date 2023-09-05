package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.Wishlist;
import com.serhiihurin.shop.online_shop.services.EmailService;
import com.serhiihurin.shop.online_shop.services.ProductService;
import com.serhiihurin.shop.online_shop.services.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WishlistFacadeImpl implements WishlistFacade{
    private final WishlistService wishlistService;
    private final ProductService productService;
    private final EmailService emailService;

    @Override
    public void notifyUsersAboutSales() {
        log.info("Started checking for sales process at: {}", LocalDateTime.now());
        List<Wishlist> wishlists = wishlistService.getWishlistsByProductsOnSale();
        if (!wishlists.isEmpty()) {
            for (Wishlist item : wishlists) {
                emailService.sendNotificationEmailABoutProductsOnSale(item.getUser().getEmail(), item.getProduct());
            }
        }
    }

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
