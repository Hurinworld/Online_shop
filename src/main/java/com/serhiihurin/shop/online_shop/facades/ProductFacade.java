package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.entity.Product;

import java.util.List;

public interface ProductFacade {
    List<Product> getAllProducts();

    List<Product> getAllProductsByShopId(Long id);

    Product getProduct(Long id);

    Product addProduct(Long shopId, ProductRequestDTO productRequestDTO);

    void increaseProductAmount(Long productId, Integer amount);

    void putProductOnSale(Long productId, int discountPercent);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);

    void deleteProduct(Long id);
}
