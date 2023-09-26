package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.enums.SortingType;
import com.serhiihurin.shop.online_shop.facades.ProductFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
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

    //TODO 'products'
    @GetMapping("/product")
    public List<ProductResponseDTO> searchProduct(
            //TODO can be nullable
            @RequestParam String productName,
            @RequestParam(required = false) @Nullable String sortingTypeValue,
            @RequestParam(required = false) @Nullable Double minimalPrice,
            @RequestParam(required = false) @Nullable Double maximalPrice
    ) {
        SortingType sortingType = null;
        if (sortingTypeValue != null) {
            sortingType = SortingType.valueOf(sortingTypeValue.toUpperCase());
        }
        return productFacade.searchProducts(productName, sortingType, minimalPrice, maximalPrice);
    }

    //TODO add endpoint for shop-owners
}
