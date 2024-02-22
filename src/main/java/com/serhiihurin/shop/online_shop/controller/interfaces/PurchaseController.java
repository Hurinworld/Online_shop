package com.serhiihurin.shop.online_shop.controller.interfaces;

import com.serhiihurin.shop.online_shop.dto.PurchaseAdminResponseDTO;
import com.serhiihurin.shop.online_shop.dto.PurchaseRequestDTO;
import com.serhiihurin.shop.online_shop.dto.PurchaseResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface PurchaseController {
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
    @SuppressWarnings("unused")
    List<PurchaseAdminResponseDTO> getAllPurchases();

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
    @SuppressWarnings("unused")
    List<PurchaseAdminResponseDTO> getPurchasesByClientId(@PathVariable Long clientId);

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
    @SuppressWarnings("unused")
    List<PurchaseResponseDTO> getPurchasesByClientId(User currentAuthenticatedUser);

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
    @SuppressWarnings("unused")
    PurchaseAdminResponseDTO getPurchase(@PathVariable Long id);

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
    @SuppressWarnings("unused")
    PurchaseResponseDTO makePurchase(
            User currentAuthenticatedUser,
            @RequestBody PurchaseRequestDTO purchaseRequestDTO
    );

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
    @SuppressWarnings("unused")
    ResponseEntity<Void> deletePurchase(@PathVariable Long id);
}
