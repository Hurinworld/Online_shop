package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.PurchaseAdminResponseDTO;
import com.serhiihurin.shop.online_shop.dto.PurchaseRequestDTO;
import com.serhiihurin.shop.online_shop.dto.PurchaseResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.facades.PurchaseFacadeImpl;
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
@RequestMapping("/online-shop/purchases")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'CLIENT')")
@Tag(name = "Purchase")
@RequiredArgsConstructor
public class PurchaseRESTController {
    private final PurchaseFacadeImpl purchaseFacade;
    private final ModelMapper modelMapper;

    @Operation(
            description = "GET all purchases endpoint for admin",
            summary = "endpoint to retrieve all purchases",
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
    public List<PurchaseAdminResponseDTO> getAllPurchases() {
        return modelMapper.map(
                purchaseFacade.getAllPurchases(),
                new TypeToken<List<PurchaseAdminResponseDTO>>() {
                }.getType()
        );
    }

    @Operation(
            description = "GET endpoint for admin",
            summary = "endpoint to retrieve purchases by client ID",
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
                            name = "clientId",
                            description = "ID of client",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyAuthority('client view info', 'admin view info')")
    public List<PurchaseAdminResponseDTO> getPurchasesByClientId(@PathVariable Long clientId) {
        return modelMapper.map(
                purchaseFacade.getPurchasesByClientId(clientId),
                new TypeToken<List<PurchaseAdminResponseDTO>>() {
                }.getType()
        );
    }

    @Operation(
            description = "GET endpoint for client",
            summary = "endpoint to retrieve yourself purchases",
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
    @GetMapping("/client/me")
    @PreAuthorize("hasAnyAuthority('client view info', 'admin view info')")
    public List<PurchaseResponseDTO> getPurchasesByClientId(User currentAuthenticatedUser) {
        return modelMapper.map(
                purchaseFacade.getPurchasesByClientId(currentAuthenticatedUser.getId()),
                new TypeToken<List<PurchaseResponseDTO>>() {
                }.getType()
        );
    }

    @Operation(
            description = "GET purchase endpoint for admin",
            summary = "endpoint to retrieve purchase by ID",
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
                            description = "ID value of purchase needed for getting info from database",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin view info')")
    public PurchaseAdminResponseDTO getPurchase(@PathVariable Long id) {
        return modelMapper.map(
                purchaseFacade.getPurchase(id),
                PurchaseAdminResponseDTO.class
        );
    }

    @Operation(
            description = "POST endpoint for client",
            summary = "endpoint for making a purchase",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad request",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    )
            },
            parameters = {
                    @Parameter(
                            name = "clientId",
                            description = "The ID of client that makes a purchase",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "A request DTO with product IDs and count of each product for making purchase",
                    required = true
            )
    )
    @PostMapping
    @PreAuthorize("hasAuthority('purchase creation')")
    public PurchaseResponseDTO makePurchase(User currentAuthenticatedUser,
                                            @RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        return modelMapper.map(
                purchaseFacade.makePurchase(currentAuthenticatedUser, purchaseRequestDTO),
                PurchaseResponseDTO.class
        );
    }

    @Operation(
            description = "DELETE purchase endpoint for admin",
            summary = "endpoint for admin to delete purchase",
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
                            description = "ID value of purchase to be deleted from database",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('purchase management')")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        purchaseFacade.deletePurchase(id);
        return ResponseEntity.ok().build();
    }
}
