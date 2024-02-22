package com.serhiihurin.shop.online_shop.controller.interfaces;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductController {
    @Operation(
            description = "GET all products endpoint for admin",
            summary = "endpoint to retrieve all products",
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
            }
    )
    @SuppressWarnings("unused")
    List<ProductResponseDTO> getAllProducts();

    @Operation(
            description = "GET endpoint for admin and shop owner",
            summary = "endpoint to retrieve products by shop ID",
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
                            name = "shopId",
                            description = "ID of shop",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @SuppressWarnings("unused")
    List<ProductResponseDTO> getAllProductsByShopId(@PathVariable Long id);

    @Operation(
            description = "GET product endpoint for admin and shop owner",
            summary = "endpoint to retrieve product by ID",
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
                            name = "id",
                            description = "ID value of product needed for getting info from database",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @SuppressWarnings("unused")
    ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id);

    @Operation(
            description = "POST endpoint for shop owner",
            summary = "endpoint to add new product",
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
                            name = "shopId",
                            description = "The ID of shop for which product should be added",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "A request DTO with information required to make a product",
                    required = true
            )
    )
    @SuppressWarnings("unused")
    ResponseEntity<ProductResponseDTO> addNewProduct(
            User currentAuthenticatedUser,
            @RequestPart("product-request-dto") ProductRequestDTO productRequestDTO,
            @RequestPart("files") MultipartFile[] files
    );

    @SuppressWarnings("unused")
    ResponseEntity<Void> putProductOnSale(
            User currentAuthenticatedUser, @PathVariable Long productId, @RequestParam int discountPercent
    );

    @Operation(
            description = "PATCH endpoint for shop owner",
            summary = "endpoint to update product",
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
                            name = "id",
                            description = "ID of product to be updated",
                            in = ParameterIn.PATH,
                            required = true
                    )
            },
            requestBody =
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "A request DTO with new product",
                    required = true
            )
    )
    @SuppressWarnings("unused")
    ResponseEntity<ProductResponseDTO> updateProduct(
            User currentAuthenticatedUser,
            @PathVariable Long productId,
            @RequestBody ProductRequestDTO productRequestDTO
    );

    @SuppressWarnings("unused")
    ResponseEntity<Void> removeProductFromSale(User currentAuthenticatedUser, @PathVariable Long productId);

    @Operation(
            description = "DELETE product endpoint for shop owner and super admin",
            summary = "endpoint for for shop owner and super admin to delete product",
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
                    ),
            },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID value of product to be deleted from database",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @SuppressWarnings("unused")
    ResponseEntity<Void> deleteProduct(@PathVariable Long productId, User currentAuthenticatedUser);

    @SuppressWarnings("unused")
    ResponseEntity<Void> subscribeForNotificationAboutAvailability(
            @PathVariable Long productId,
            User currentAuthenticatedUser
    );
}
