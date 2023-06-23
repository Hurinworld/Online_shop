package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.Product;

public interface ProductFacade {
    Product addProduct(Long productDataId, Product product);

    Product updateProduct(Product product);
}
