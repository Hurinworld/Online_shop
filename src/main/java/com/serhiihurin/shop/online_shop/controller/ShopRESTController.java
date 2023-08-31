package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ShopRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ShopResponseDTO;
import com.serhiihurin.shop.online_shop.facades.ShopFacade;
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
@RequestMapping("/online-shop/shops")
@PreAuthorize("hasAnyRole('SHOP_OWNER', 'ADMIN', 'SUPER_ADMIN')")
@Tag(name = "Shop")
@RequiredArgsConstructor
public class ShopRESTController {
    private final ShopFacade shopFacade;
    private final ModelMapper modelMapper;

    @Operation(
            description = "GET all shops endpoint for admin",
            summary = "endpoint to retrieve all shops",
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
    public List<ShopResponseDTO> getAllShops() {
        return modelMapper.map(
                shopFacade.getAllShops(),
                new TypeToken<List<ShopResponseDTO>>() {
                }.getType()
        );
    }

    @Operation(
            description = "GET shop endpoint for admin",
            summary = "endpoint to retrieve shop by ID",
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
                            description = "ID value of shop needed for getting info from database",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin view info')")
    public ShopResponseDTO getShop(@PathVariable Long id) {
        return modelMapper.map(
                shopFacade.getShop(id),
                ShopResponseDTO.class
        );
    }

    @Operation(
            description = "POST endpoint for shop owner",
            summary = "endpoint to add new shop",
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
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "A request DTO with information required to add new shop",
                    required = true
            )
    )
    @PostMapping
    @PreAuthorize("hasAuthority('shop management')")
    public ResponseEntity<ShopResponseDTO> addNewShop(@RequestBody ShopRequestDTO shopRequestDTO) {
        return ResponseEntity.ok(
                modelMapper.map(
                        shopFacade.saveShop(shopRequestDTO),
                        ShopResponseDTO.class
                )
        );
    }

    @Operation(
            description = "PUT endpoint for shop owner",
            summary = "endpoint to update shop information",
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
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "A request DTO with information required to update shop info",
                    required = true
            )
    )
    @PutMapping
    @PreAuthorize("hasAuthority('shop management')")
    public ResponseEntity<ShopResponseDTO> updateShop(@RequestBody ShopRequestDTO shopRequestDTO) {
        return ResponseEntity.ok(
                modelMapper.map(
                        shopFacade.updateShop(shopRequestDTO),
                        ShopResponseDTO.class
                )
        );
    }

    @Operation(
            description = "DELETE shop endpoint for shop owner and super admin",
            summary = "endpoint for shop owner and super admin to delete shop",
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
                            description = "ID value of shop to be deleted from database",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('shop management', 'super admin info deletion')")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopFacade.deleteShop(id);
        return ResponseEntity.ok().build();
    }
}
