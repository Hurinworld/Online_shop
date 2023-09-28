package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.Wishlist;
import com.serhiihurin.shop.online_shop.services.EmailService;
import com.serhiihurin.shop.online_shop.services.NotificationService;
import com.serhiihurin.shop.online_shop.services.ProductService;
import com.serhiihurin.shop.online_shop.services.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final NotificationService notificationService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void notifyUsersAboutSales() {
        log.info("Started checking for sales process at: {}", LocalDateTime.now());
        List<Wishlist> wishlists = wishlistService.getWishlistsByProductsOnSale();

        Map<User, List<Product>> sortedByUserWishlists = new HashMap<>();

        if (!wishlists.isEmpty()) {
            for (Wishlist item : wishlists) {
                List<Product> productList =
                        sortedByUserWishlists.getOrDefault(item.getUser(), new ArrayList<>());
                productList.add(item.getProduct());
                sortedByUserWishlists.put(item.getUser(), productList);
            }
            for (User user : sortedByUserWishlists.keySet()) {
                emailService
                        .sendNotificationEmailAboutProductsOnSale(
                                user.getEmail(),
                                sortedByUserWishlists.get(user)
                        );
                notificationService.addNotification(
                        "Notification about products form wishlist on sale",
                        "Hi " + user.getFirstName() + "! "
                                + "\n\nSome products from your wishlist are now on sale: \n"
                                + sortedByUserWishlists.get(user)
                );
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
    public void deleteProductFromWishlist(Long userId, Long productId) {
        wishlistService.deleteProductFromWishlist(userId, productId);
    }
}
