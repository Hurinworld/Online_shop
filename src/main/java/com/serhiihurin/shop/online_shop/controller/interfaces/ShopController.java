package com.serhiihurin.shop.online_shop.controller.interfaces;

import com.serhiihurin.shop.online_shop.dto.ShopRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ShopResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ShopController {
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
    @SuppressWarnings("unused")
    List<ShopResponseDTO> getAllShops();

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
    @SuppressWarnings("unused")
    ShopResponseDTO getShop(@PathVariable Long id);

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
    @SuppressWarnings("unused")
    ResponseEntity<ShopResponseDTO> addNewShop(
            User currentAuthenticatedUser,
            @RequestBody ShopRequestDTO shopRequestDTO
    );

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
    @SuppressWarnings("unused")
    ResponseEntity<ShopResponseDTO> updateShop(@RequestBody ShopRequestDTO shopRequestDTO);

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
    @SuppressWarnings("unused")
    ResponseEntity<Void> deleteShop(@PathVariable Long id);
}
