package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Event;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    List<Product> getProductsByShopId(Long id);

    Product getProduct(Long id);

    Product addProduct(Product product);

    Product updateProduct(
            User currentAuthenticatedUser,
            ProductRequestDTO productRequestDTO,
            Product product
    );

    void increaseProductAmount(User currentAuthenticatedUser, Product product, Integer amount);

    <T> void putProductOnSale(User currentAuthenticatedUser, T productSearchValue, int discountPercent);

    void removeProductFromSale(User currentAuthenticatedUser, Long productId);

    void removeEventProductsFromSale(Long eventId);

    void deleteProduct(User currentAuthenticatedUser, Long id);
}
