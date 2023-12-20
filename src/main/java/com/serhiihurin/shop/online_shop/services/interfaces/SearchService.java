package com.serhiihurin.shop.online_shop.services.interfaces;

import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.dto.SearchRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Product;

import java.util.List;

public interface SearchService {
    List<Product> searchProductsGlobally(SearchRequestDTO searchRequestDTO);

    List<Product> searchProductsInShop(SearchRequestDTO searchRequestDTO);
}
