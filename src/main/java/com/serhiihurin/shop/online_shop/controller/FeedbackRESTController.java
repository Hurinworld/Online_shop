package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.FeedbackResponseDTO;
import com.serhiihurin.shop.online_shop.dto.FeedbackUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.facades.interfaces.FeedbackFacade;
import com.serhiihurin.shop.online_shop.dto.FeedbackRequestDTO;
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
@RequestMapping("/online-shop/feedbacks")
@PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
@Tag(name = "Feedback")
@RequiredArgsConstructor
public class FeedbackRESTController {
    private final FeedbackFacade feedbackFacade;
    private final ModelMapper modelMapper;

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
    @GetMapping
    @PreAuthorize("hasAuthority('admin view info')")
    public List<FeedbackResponseDTO> getAllFeedbacks() {
        return modelMapper.map(
                feedbackFacade.getAllFeedbacks(),
                new TypeToken<List<FeedbackResponseDTO>>() {
                }.getType()
        );
    }

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
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin view info')")
    public FeedbackResponseDTO getFeedback(@PathVariable Long id) {
        return modelMapper.map(feedbackFacade.getFeedback(id), FeedbackResponseDTO.class);
    }

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
    @GetMapping("/product-feedbacks")
    @PreAuthorize("hasAnyAuthority('admin view info', 'client view info')")
    public List<FeedbackResponseDTO> getAllFeedbacksByProduct(@RequestParam Long productId) {
        return modelMapper.map(
                feedbackFacade.getAllFeedbacksByProduct(productId),
                new TypeToken<List<FeedbackResponseDTO>>() {
                }.getType()
        );
    }

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
    @GetMapping("/client-feedbacks")
    @PreAuthorize("hasAuthority('admin view info')")
    public List<FeedbackResponseDTO> getAllFeedbacksByClient(@RequestParam Long userId) {
        return modelMapper.map(
                feedbackFacade.getAllFeedbacksByClient(userId),
                new TypeToken<List<FeedbackResponseDTO>>() {
                }.getType()
        );
    }

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
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('client view info')")
    public List<FeedbackResponseDTO> getAllFeedbacksByClient(User currentAuthenticatedUser) {
        return modelMapper.map(
                feedbackFacade.getAllFeedbacksByClient(currentAuthenticatedUser.getId()),
                new TypeToken<List<FeedbackResponseDTO>>() {
                }.getType()
        );
    }

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
    @PostMapping()
    @PreAuthorize("hasAuthority('feedback management')")
    public ResponseEntity<FeedbackResponseDTO> addNewFeedback(User currentAuthenticatedUser,
                                                              @RequestBody FeedbackRequestDTO feedbackRequestDto) {
        return ResponseEntity.ok(
                modelMapper.map(
                        feedbackFacade.addFeedback(currentAuthenticatedUser.getId(), feedbackRequestDto),
                        FeedbackResponseDTO.class
                )
        );
    }

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
    @PatchMapping
    @PreAuthorize("hasAuthority('feedback management')")
    public ResponseEntity<FeedbackResponseDTO> updateFeedback(
            @RequestParam Long id,
            @RequestBody FeedbackUpdateRequestDTO feedbackUpdateRequestDTO
    ) {
        return ResponseEntity.ok(
                modelMapper.map(
                        feedbackFacade.updateFeedback(id, feedbackUpdateRequestDTO),
                        FeedbackResponseDTO.class
                )
        );
    }

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
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin info deletion', 'feedback management')")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackFacade.deleteFeedback(id);
        return ResponseEntity.ok().build();
    }
}
