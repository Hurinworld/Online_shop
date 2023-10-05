package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;

import java.util.List;

public interface SearchService {
    List<ProductResponseDTO> searchProductsGlobally(
            String productName,
            String sortingParameterValue,
            String sortingTypeValue,
            Double minimalPrice,
            Double maximalPrice
    );

    List<ProductResponseDTO> searchProductsInShop(
            User currentAuthenticatedUser,
            String productName,
            String sortingParameterValue,
            String sortingTypeValue,
            Double minimalPrice,
            Double maximalPrice
    );
}
