package com.serhiihurin.shop.online_shop.controller.interfaces;

import com.serhiihurin.shop.online_shop.dto.EventRequestDTO;
import com.serhiihurin.shop.online_shop.dto.EventResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface EventController {
    @Operation(
            description = "GET endpoint for admin and shop owner",
            summary = "endpoint to retrieve info about events by specific creator",
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
    ResponseEntity<List<EventResponseDTO>> getEventsByEventCreatorId(User currentAuthenticatedUser);

    @Operation(
            description = "POST endpoint for shop owner",
            summary = "endpoint to add new event",
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
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "A request DTO with information required to make an event",
                    required = true
            )
    )
    @SuppressWarnings("unused")
    ResponseEntity<EventResponseDTO> createEvent(
            User currentAuthenticatedUser,
            @RequestBody EventRequestDTO eventRequestDTO
    );

    @Operation(
            description = "DELETE endpoint for admin and shop owner",
            summary = "endpoint to delete event by its ID",
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
                            name = "eventId",
                            description = "The ID of event to be deleted",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @SuppressWarnings("unused")
    ResponseEntity<Void> deleteEvent(User currentAuthenticatedUser, @PathVariable Long eventId);
}
