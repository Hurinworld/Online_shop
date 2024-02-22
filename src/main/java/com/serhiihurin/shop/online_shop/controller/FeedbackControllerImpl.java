package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.controller.interfaces.FeedbackController;
import com.serhiihurin.shop.online_shop.dto.FeedbackResponseDTO;
import com.serhiihurin.shop.online_shop.dto.FeedbackUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.facades.interfaces.FeedbackFacade;
import com.serhiihurin.shop.online_shop.dto.FeedbackRequestDTO;
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
public class FeedbackControllerImpl implements FeedbackController {
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

    @GetMapping("/product-feedbacks")
    @PreAuthorize("hasAnyAuthority('admin view info', 'client view info')")
    public List<FeedbackResponseDTO> getAllFeedbacksByProduct(@RequestParam Long productId) {
        return modelMapper.map(
                feedbackFacade.getAllFeedbacksByProduct(productId),
                new TypeToken<List<FeedbackResponseDTO>>() {
                }.getType()
        );
    }

    @GetMapping("/client-feedbacks")
    @PreAuthorize("hasAuthority('admin view info')")
    public List<FeedbackResponseDTO> getAllFeedbacksByClient(@RequestParam Long userId) {
        return modelMapper.map(
                feedbackFacade.getAllFeedbacksByClient(userId),
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
    public ResponseEntity<FeedbackResponseDTO> addNewFeedback(
            User currentAuthenticatedUser,
            @RequestBody FeedbackRequestDTO feedbackRequestDto
    ) {
        return ResponseEntity.ok(
                modelMapper.map(
                        feedbackFacade.addFeedback(currentAuthenticatedUser.getId(), feedbackRequestDto),
                        FeedbackResponseDTO.class
                )
        );
    }

    @PatchMapping
    //TODO fix issue with rate enum value in db //done
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
