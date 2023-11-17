package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.dto.SearchRequestDTO;

import java.util.List;

public interface SearchService {
    List<ProductResponseDTO> searchProductsGlobally(SearchRequestDTO searchRequestDTO);

    List<ProductResponseDTO> searchProductsInShop(SearchRequestDTO searchRequestDTO);
}
