package com.serhiihurin.shop.online_shop.controller.interfaces;

import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SearchController {
    @Operation(
            description = "public Get endpoint",
            summary = """
                    endpoint to perform search of products globally.
                    Returns list of filtered and sorted by specific parameter products
                    """,
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad syntax",
                            responseCode = "400"
                    )
            },
            parameters = {
                    @Parameter(
                            name = "productName",
                            description = "Full name of product or part of it to search product",
                            in = ParameterIn.QUERY
                    ),
                    @Parameter(
                            name = "sortingParameterValue",
                            description = "Parameter by which products sorting will be performed",
                            in = ParameterIn.QUERY
                    ),
                    @Parameter(
                            name = "sortingDirection",
                            description = "Parameter which specifies direction of sorting",
                            in = ParameterIn.QUERY
                    ),
                    @Parameter(
                            name = "minimalPrice",
                            description = "minimal product price",
                            in = ParameterIn.QUERY
                    ),
                    @Parameter(
                            name = "maximalPrice",
                            description = "maximal product price",
                            in = ParameterIn.QUERY
                    ),
            }
    )
    @SuppressWarnings("unused")
    List<ProductResponseDTO> searchProductsGlobally(
            @RequestParam(required = false) @Nullable String productName,
            @RequestParam(required = false) @Nullable String sortingParameterValue,
            @RequestParam(required = false) @Nullable String sortingDirection,
            @RequestParam(required = false) @Nullable Double minimalPrice,
            @RequestParam(required = false) @Nullable Double maximalPrice
    );

    @Operation(
            description = "Get endpoint for shop owner",
            summary = """
                    endpoint to perform search of products by shop owner in his shop.
                    Returns list of filtered and sorted by specific parameter products
                    """,
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad syntax",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    )
            },
            parameters = {
                    @Parameter(
                            name = "currentAuthenticatedUser",
                            description = "Current authenticated user object",
                            in = ParameterIn.QUERY,
                            required = true
                    ),
                    @Parameter(
                            name = "productName",
                            description = "Full name of product or part of it to search product",
                            in = ParameterIn.QUERY
                    ),
                    @Parameter(
                            name = "sortingParameterValue",
                            description = "Parameter by which products sorting will be performed",
                            in = ParameterIn.QUERY
                    ),
                    @Parameter(
                            name = "sortingDirection",
                            description = "Parameter which specifies direction of sorting",
                            in = ParameterIn.QUERY
                    ),
                    @Parameter(
                            name = "minimalPrice",
                            description = "minimal product price",
                            in = ParameterIn.QUERY
                    ),
                    @Parameter(
                            name = "maximalPrice",
                            description = "maximal product price",
                            in = ParameterIn.QUERY
                    ),
            }
    )
    @SuppressWarnings("unused")
    List<ProductResponseDTO> searchProductsInShop(
            User currentAuthenticatedUser,
            @RequestParam(required = false) @Nullable String productName,
            @RequestParam(required = false) @Nullable String sortingParameterValue,
            @RequestParam(required = false) @Nullable String sortingDirection,
            @RequestParam(required = false) @Nullable Double minimalPrice,
            @RequestParam(required = false) @Nullable Double maximalPrice
    );
}
