package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.Shop;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    List<Product> getProductsByShopId(Long id);

    Product getProduct(Long id);

    Product saveProduct(Product product);

    Product updateProduct(
            ProductRequestDTO productRequestDTO,
            Shop shop,
            Product product
    );

    void increaseProductAmount(Product product, Integer amount);

    void deleteProduct(Long id);
}
