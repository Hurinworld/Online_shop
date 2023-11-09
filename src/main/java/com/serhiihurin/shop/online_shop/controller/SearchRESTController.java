package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.facades.ProductFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/online-shop/search")
@Tag(name = "Search")
@RequiredArgsConstructor
public class SearchRESTController {
    private final ProductFacade productFacade;

    @GetMapping("/products")
    public List<ProductResponseDTO> searchProductsGlobally(
            User currentAuthenticatedUser,
            @RequestParam(required = false) @Nullable String productName,
            @RequestParam(required = false) @Nullable String sortingParameterValue,
            @RequestParam(required = false) @Nullable String sortingTypeValue,
            @RequestParam(required = false) @Nullable Double minimalPrice,
            @RequestParam(required = false) @Nullable Double maximalPrice
    ) {
        // TODO map params to dto
        return productFacade.searchProductsGlobally(
                productName,
                sortingParameterValue,
                sortingTypeValue,
                minimalPrice,
                maximalPrice);
    }

    @GetMapping("/shop/products")
    @PreAuthorize("hasAuthority('shop management')")
    public List<ProductResponseDTO> searchProductsInShop(
            User currentAuthenticatedUser,
            @RequestParam(required = false) @Nullable String productName,
            @RequestParam(required = false) @Nullable String sortingParameterValue,
            @RequestParam(required = false) @Nullable String sortingTypeValue,
            @RequestParam(required = false) @Nullable Double minimalPrice,
            @RequestParam(required = false) @Nullable Double maximalPrice
    ) {
        // TODO map params to dto

        return productFacade.searchProductsInShop(
                currentAuthenticatedUser,
                productName,
                sortingParameterValue,
                sortingTypeValue,
                minimalPrice,
                maximalPrice
        );
    }
}
