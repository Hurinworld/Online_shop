package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.facades.interfaces.FileFacade;
import com.serhiihurin.shop.online_shop.facades.interfaces.ProductFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/online-shop/products")
@PreAuthorize("hasAnyRole('ADMIN', 'SHOP_OWNER', 'CLIENT', 'SUPER_ADMIN')")
@Tag(name = "Product")
@RequiredArgsConstructor
public class ProductRESTController {
    private final ProductFacade productFacade;
    private final FileFacade fileFacade;
    //TODO delete
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
        return productFacade.getAllProducts();
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
        return productFacade.getAllProductsByShopId(id);
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
    @PreAuthorize("hasAnyAuthority('shop owner view info', 'admin view info', 'client view info')")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productFacade.getProduct(id));
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
    public ResponseEntity<ProductResponseDTO> addNewProduct(
            User currentAuthenticatedUser,
            @RequestPart("product-request-dto") ProductRequestDTO productRequestDTO,
            @RequestPart("files")MultipartFile[] files
            ) {
        ProductResponseDTO productResponseDTO = productFacade.addProduct(
                currentAuthenticatedUser, productRequestDTO
        );
        productResponseDTO.setImagesEndpoints(fileFacade.saveProductImages(
                currentAuthenticatedUser.getId(),
                productResponseDTO.getId(),
                files
        ));

        return ResponseEntity.ok(productResponseDTO);
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

    @PostMapping("/{productId}/sale")
    @PreAuthorize("hasAuthority('product management')")
    public ResponseEntity<Void> putProductOnSale(
            User currentAuthenticatedUser, @PathVariable Long productId, @RequestParam int discountPercent
    ) {
        productFacade.putProductOnSale(currentAuthenticatedUser, productId, discountPercent);
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
    @PatchMapping("/{productId}")
    @PreAuthorize("hasAuthority('product management')")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            User currentAuthenticatedUser,
            @PathVariable Long productId,
            @RequestBody ProductRequestDTO productRequestDTO
    ) {
        return ResponseEntity.ok(
                productFacade.updateProduct(currentAuthenticatedUser, productId, productRequestDTO)
        );
    }

    @DeleteMapping("/{productId}/sale")
    @PreAuthorize("hasAuthority('product management')")
    public ResponseEntity<Void> removeProductFromSale(User currentAuthenticatedUser, @PathVariable Long productId) {
        productFacade.removeProductFromSale(currentAuthenticatedUser, productId);
        return ResponseEntity.ok().build();
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
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('product management', 'super admin info deletion')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId, User currentAuthenticatedUser) {
        productFacade.deleteProduct(currentAuthenticatedUser, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/availability/{productId}")
    public ResponseEntity<Void> subscribeForNotificationAboutAvailability(
            @PathVariable Long productId,
            User currentAuthenticatedUser
    ) {
        productFacade.subscribeForNotificationAboutAvailability(productId, currentAuthenticatedUser);
        return ResponseEntity.ok().build();
    }
}
