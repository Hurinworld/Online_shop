package com.serhiihurin.shop.online_shop.controller.interfaces;

import com.serhiihurin.shop.online_shop.dto.WishlistResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface WishlistController {
    @Operation(
            description = "GET endpoint for client and shop owner",
            summary = "endpoint to retrieve wishlist of user which making particular request",
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
                    )
            }
    )
    @SuppressWarnings("unused")
    WishlistResponseDTO getUserWishlist(User currentAuthenticatedUser);

    @Operation(
            description = "POST endpoint for client and shop owner",
            summary = "endpoint to add new product to wishlist",
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
                            name = "productId",
                            description = "The ID of product to be added to wishlist",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            }
    )
    @SuppressWarnings("unused")
    ResponseEntity<Void> addProductToWishlist(User currentAuthenticatedUser, @RequestParam Long productId);

    @Operation(
            description = "DELETE endpoint for client and shop owner",
            summary = "endpoint to delete product from wishlist",
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
                            name = "productId",
                            description = "The ID of product to be deleted from wishlist",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            }
    )
    @SuppressWarnings("unused")
    ResponseEntity<Void> deleteProductFromWishlist(User currentAuthenticatedUser, @PathVariable Long productId);
}
