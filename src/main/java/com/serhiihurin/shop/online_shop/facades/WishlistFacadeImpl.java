package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.Wishlist;
import com.serhiihurin.shop.online_shop.services.EmailService;
import com.serhiihurin.shop.online_shop.services.ProductService;
import com.serhiihurin.shop.online_shop.services.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<String> ignoreList = emailService.getIgnoreList();

        Map<User, List<Product>> sortedByUserWishlists = new HashMap<>();

        if (!wishlists.isEmpty()) {
            for (Wishlist item : wishlists) {
                List<Product> productList =
                        sortedByUserWishlists.getOrDefault(item.getUser(), new ArrayList<>());
                productList.add(item.getProduct());
                sortedByUserWishlists.put(item.getUser(), productList);
            }
            for (User user : sortedByUserWishlists.keySet()) {
                if (!ignoreList.contains(user.getEmail())) {
                    emailService
                            .sendNotificationEmailABoutProductsOnSale(
                                    user.getEmail(),
                                    sortedByUserWishlists.get(user)
                            );
                    ignoreList.add(user.getEmail());
                }
            }
            emailService.setIgnoreList(ignoreList);
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
