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

    Product addProduct(ProductRequestDTO productRequestDTO);

    Product updateProduct(
            User currentAuthenticatedUser,
            ProductRequestDTO productRequestDTO,
            Product product
    );

    void putProductOnSale(User currentAuthenticatedUser, Object productSearchValue, int discountPercent, Event event);

    void removeProductFromSale(User currentAuthenticatedUser, Long productId);

    void removeEventProductsFromSale(Long eventId);

    void deleteProduct(User currentAuthenticatedUser, Long id);
}
