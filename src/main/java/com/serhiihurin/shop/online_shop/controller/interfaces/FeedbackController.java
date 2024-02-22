package com.serhiihurin.shop.online_shop.controller.interfaces;

import com.serhiihurin.shop.online_shop.dto.FeedbackRequestDTO;
import com.serhiihurin.shop.online_shop.dto.FeedbackResponseDTO;
import com.serhiihurin.shop.online_shop.dto.FeedbackUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface FeedbackController {
    @Operation(
            description = "GET all feedbacks endpoint for admin",
            summary = "endpoint to retrieve all user feedbacks",
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
    List<FeedbackResponseDTO> getAllFeedbacks();

    @Operation(
            description = "GET feedback endpoint for admin",
            summary = "endpoint to retrieve feedback by ID",
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
                            description = "ID value of feedback needed for getting info from database",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @SuppressWarnings("unused")
    FeedbackResponseDTO getFeedback(@PathVariable Long id);

    @Operation(
            description = "GET endpoint for all users",
            summary = "endpoint to retrieve feedbacks by product",
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
                            description = "ID of product",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            }
    )
    @SuppressWarnings("unused")
    List<FeedbackResponseDTO> getAllFeedbacksByProduct(@RequestParam Long productId);

    @Operation(
            description = "GET endpoint for admin to get feedbacks by user ID",
            summary = "endpoint to retrieve feedbacks by user",
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
                            name = "userId",
                            description = "ID of user",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            }
    )
    @SuppressWarnings("unused")
    List<FeedbackResponseDTO> getAllFeedbacksByClient(@RequestParam Long userId);

    @Operation(
            description = "GET endpoint for all users",
            summary = "endpoint to retrieve all feedbacks made from self user account",
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
    List<FeedbackResponseDTO> getAllFeedbacksByClient(User currentAuthenticatedUser);

    @Operation(
            description = "POST endpoint for all users",
            summary = "endpoint to leave a feedback",
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
                    description = "A request DTO with information required to make a feedback",
                    required = true
            )
    )
    @SuppressWarnings("unused")
    ResponseEntity<FeedbackResponseDTO> addNewFeedback(
            User currentAuthenticatedUser,
            @RequestBody FeedbackRequestDTO feedbackRequestDto
    );

    @Operation(
            description = "PATCH endpoint for all users",
            summary = "endpoint to update feedback info",
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
                            description = "ID of feedback to be updated",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            },
            requestBody =
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "A request DTO with new feedback information",
                    required = true
            )
    )
    @SuppressWarnings("unused")
    ResponseEntity<FeedbackResponseDTO> updateFeedback(
            @RequestParam Long id,
            @RequestBody FeedbackUpdateRequestDTO feedbackUpdateRequestDTO
    );

    @Operation(
            description = "DELETE feedback endpoint for all users",
            summary = "endpoint for users to delete their feedbacks",
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
                            description = "ID value of feedback to be deleted from database",
                            in = ParameterIn.PATH,
                            required = true
                    )
            }
    )
    @SuppressWarnings("unused")
    ResponseEntity<Void> deleteFeedback(@PathVariable Long id);
}
