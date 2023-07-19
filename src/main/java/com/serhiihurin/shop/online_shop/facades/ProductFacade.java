package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Product;

public interface ProductFacade {
    Product addProduct(ProductRequestDTO productRequestDTO);

    Product updateProduct(ProductUpdateRequestDTO productUpdateRequestDTO);

    void deleteProduct(Long id);
}
