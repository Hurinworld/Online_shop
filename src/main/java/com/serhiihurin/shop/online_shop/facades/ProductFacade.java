package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;

import java.util.List;

public interface ProductFacade {
    List<Product> getAllProducts();

    List<Product> getAllProductsByShopId(Long id);

    Product getProduct(Long id);

    Product addProduct(User currentAuthenticatedUser, ProductRequestDTO productRequestDTO);

    void putProductOnSale(User currentAuthenticatedUser, Long productId, int discountPercent);

    Product updateProduct(User currentAuthenticatedUser, Long id, ProductRequestDTO productRequestDTO);

    void removeProductFromSale(User currentAuthenticatedUser, Long productId);

    void deleteProduct(User currentAuthenticatedUser, Long id);
}
