package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Event;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.enums.SortingType;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    List<Product> getProductsByShopId(Long id);

    List<Product> searchProducts(String productName, SortingType sortingType, Double minimalPrice, Double maximalPrice);

    Product getProduct(Long id);

    Product addProduct(Product product);

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
