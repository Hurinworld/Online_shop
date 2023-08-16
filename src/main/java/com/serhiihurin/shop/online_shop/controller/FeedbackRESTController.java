package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.FeedbackResponseDTO;
import com.serhiihurin.shop.online_shop.dto.FeedbackUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.facades.FeedbackFacade;
import com.serhiihurin.shop.online_shop.dto.FeedbackRequestDTO;
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
@RequiredArgsConstructor
public class FeedbackRESTController {
    private final FeedbackFacade feedbackFacade;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('admin view info')")
    public List<FeedbackResponseDTO> getAllFeedbacks() {
        return modelMapper.map(
                feedbackFacade.getAllFeedbacks(),
                new TypeToken<List<FeedbackResponseDTO>>() {
                }.getType()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin view info')")
    public FeedbackResponseDTO getFeedback(@PathVariable Long id) {
        return modelMapper.map(feedbackFacade.getFeedback(id), FeedbackResponseDTO.class);
    }

    @GetMapping("/product-data-feedbacks")
    @PreAuthorize("hasAnyAuthority('admin view info', 'client view info')")
    public List<FeedbackResponseDTO> getAllFeedbacksByProductData(@RequestParam Long productDataId) {
        return modelMapper.map(
                feedbackFacade.getAllFeedbacksByProductData(productDataId),
                new TypeToken<List<FeedbackResponseDTO>>() {
                }.getType()
        );
    }

    @GetMapping("/client-feedbacks")
    @PreAuthorize("hasAuthority('admin view info')")
    public List<FeedbackResponseDTO> getAllFeedbacksByClient(@RequestParam Long clientId) {
        return modelMapper.map(
                feedbackFacade.getAllFeedbacksByClient(clientId),
                new TypeToken<List<FeedbackResponseDTO>>() {
                }.getType()
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('client view info')")
    public List<FeedbackResponseDTO> getAllFeedbacksByClient(User currentAuthenticatedUser) {
        return modelMapper.map(
                feedbackFacade.getAllFeedbacksByClient(currentAuthenticatedUser.getId()),
                new TypeToken<List<FeedbackResponseDTO>>() {
                }.getType()
        );
    }

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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin info deletion', 'feedback management')")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackFacade.deleteFeedback(id);
        return ResponseEntity.ok().build();
    }
}
