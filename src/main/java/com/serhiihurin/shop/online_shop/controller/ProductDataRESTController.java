package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.dto.ProductDataRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductDataResponseDTO;
import com.serhiihurin.shop.online_shop.facades.ProductDataFacade;
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
@RequestMapping("/online-shop/products-data")
@PreAuthorize("hasAnyRole('ADMIN', 'SHOP_OWNER', 'CLIENT', 'SUPER_ADMIN')")
@Tag(name = "Product data")
@RequiredArgsConstructor
public class ProductDataRESTController {
    private final ProductDataFacade productDataFacade;
    private final ModelMapper modelMapper;

    @Operation(
            description = "GET all product data endpoint for admin",
            summary = "endpoint to retrieve all products data",
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
    public List<ProductDataResponseDTO> getAllProductData() {
        return modelMapper.map(
                productDataFacade.getAllProductData(),
                new TypeToken<List<ProductDataResponseDTO>>() {
                }.getType()
        );
    }

    @Operation(
            description = "GET endpoint for admin and shop owner",
            summary = "endpoint to retrieve products data by shop ID",
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
    List<ProductDataResponseDTO> getAllProductDataByShopId(@PathVariable Long id) {
        return modelMapper.map(
                productDataFacade.getAllProductDataByShopId(id),
                new TypeToken<List<ProductDataResponseDTO>>() {
                }.getType()
        );
    }

    @Operation(
            description = "GET product data endpoint for admin and shop owner",
            summary = "endpoint to retrieve product data by ID",
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
                            description = "ID value of product data needed for getting info from database",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('shop owner view info', 'admin view info')")
    public ProductDataResponseDTO getProductData(@PathVariable Long id) {
        return modelMapper.map(productDataFacade.getProductData(id), ProductDataResponseDTO.class);
    }

    @Operation(
            description = "GET endpoint for admin and shop owner",
            summary = "endpoint to retrieve products by product data ID",
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
                            name = "productDataId",
                            description = "ID of product data",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            }
    )
    @GetMapping("/products")
    @PreAuthorize("hasAnyAuthority('shop owner view info', 'admin view info')")
    public List<ProductResponseDTO> getProductsByProductDataId(@RequestParam Long productDataId) {
        return modelMapper.map(
                productDataFacade.getProductsByProductDataId(productDataId),
                new TypeToken<List<ProductResponseDTO>>() {
                }.getType()
        );
    }

    @Operation(
            description = "POST endpoint for shop owner",
            summary = "endpoint to add new product data",
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
                            description = "The ID of shop for which product data should be added",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "A request DTO with information required to make a product data",
                    required = true
            )
    )
    @PostMapping
    @PreAuthorize("hasAuthority('product data management')")
    public ResponseEntity<ProductDataResponseDTO> addNewProductData
            (
                    @RequestParam Long shopId,
                    @RequestBody ProductDataRequestDTO productDataRequestDTO
            ) {
        return ResponseEntity.ok(
                modelMapper.map(
                        productDataFacade.addProductData(shopId, productDataRequestDTO),
                        ProductDataResponseDTO.class
                )
        );
    }

    @Operation(
            description = "PATCH endpoint for shop owner",
            summary = "endpoint to update product data",
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
                            description = "ID of product data to be updated",
                            in = ParameterIn.PATH,
                            required = true
                    )
            },
            requestBody =
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "A request DTO with new product data",
                    required = true
            )
    )
    //TODO add id to path! //done
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('product data management')")
    public ResponseEntity<ProductDataResponseDTO> updateProductData(
            @PathVariable Long id,
            @RequestBody ProductDataRequestDTO productDataRequestDTO
    ) {
        return ResponseEntity.ok(
                productDataFacade.updateProductData(id, productDataRequestDTO)
        );
    }

    @Operation(
            description = "DELETE product data endpoint for shop owner and super admin",
            summary = "endpoint for for shop owner and super admin to delete product data",
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
                            description = "ID value of product data to be deleted from database",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('product data management', 'super admin info deletion')")
    public ResponseEntity<Void> deleteProductData(@PathVariable Long id) {
        productDataFacade.deleteProductData(id);
        return ResponseEntity.ok().build();
    }
}
