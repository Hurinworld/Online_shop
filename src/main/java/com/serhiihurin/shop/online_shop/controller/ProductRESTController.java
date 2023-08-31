package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.facades.ProductFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/products")
@PreAuthorize("hasAnyRole('ADMIN', 'SHOP_OWNER', 'CLIENT', 'SUPER_ADMIN')")
@Tag(name = "Product")
@RequiredArgsConstructor
public class ProductRESTController {
    private final ProductFacade productFacade;
    private final ModelMapper modelMapper;

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
    @GetMapping
    @PreAuthorize("hasAuthority('admin view info')")
    public List<ProductResponseDTO> getAllProducts() {
        return modelMapper.map(
                productFacade.getAllProducts(),
                new TypeToken<List<ProductResponseDTO>>() {
                }.getType()
        );
    }

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
    @GetMapping("/shop/{id}")
    @PreAuthorize("hasAnyAuthority('shop owner view info', 'admin view info')")
    List<ProductResponseDTO> getAllProductsByShopId(@PathVariable Long id) {
        return modelMapper.map(
                productFacade.getAllProductsByShopId(id),
                new TypeToken<List<ProductResponseDTO>>() {
                }.getType()
        );
    }

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
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('shop owner view info', 'admin view info')")
    public ProductResponseDTO getProduct(@PathVariable Long id) {
        return modelMapper.map(productFacade.getProduct(id), ProductResponseDTO.class);
    }

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
    @PostMapping
    @PreAuthorize("hasAuthority('product management')")
    public ResponseEntity<ProductResponseDTO> addNewProduct
            (
                    @RequestParam Long shopId,
                    @RequestBody ProductRequestDTO productRequestDTO
            ) {
        return ResponseEntity.ok(
                modelMapper.map(
                        productFacade.addProduct(shopId, productRequestDTO),
                        ProductResponseDTO.class
                )
        );
    }

    @Operation(
            description = "PUT endpoint for shop owner",
            summary = "endpoint to increase amount of some product",
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
                            name = "productId",
                            description = "The ID of product which amount should be increased",
                            in = ParameterIn.PATH,
                            required = true
                    ),
                    @Parameter(
                            name = "amount",
                            description = "The amount of units by which product amount should be increased",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            }
    )
    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('product management')")
    public ResponseEntity<Void> increaseProductAmount(@PathVariable Long productId, @RequestParam Integer amount) {
        productFacade.increaseProductAmount(productId, amount);
        return ResponseEntity.ok().build();
    }

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
    //TODO add id to path! //done
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('product management')")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO productRequestDTO
    ) {
        return ResponseEntity.ok(
                productFacade.updateProduct(id, productRequestDTO)
        );
    }

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
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('product management', 'super admin info deletion')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productFacade.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
