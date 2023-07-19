package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.FeedbackDTO;
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
    public List<FeedbackDTO> getAllFeedbacks() {
        return modelMapper.map(
                feedbackFacade.getAllFeedbacks(),
                new TypeToken<List<FeedbackDTO>>(){}.getType()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin view info')")
    public FeedbackDTO getFeedback(@PathVariable Long id) {
        return modelMapper.map(feedbackFacade.getFeedback(id), FeedbackDTO.class);
    }

    // TODO: 14.07.2023 convert into request param
    @GetMapping("/product-data/{id}")
    @PreAuthorize("hasAnyAuthority('admin view info', 'client view info')")
    public List<FeedbackDTO> getAllFeedbacksByProductData(@PathVariable Long id) {
        return modelMapper.map(
                feedbackFacade.getAllFeedbacksByProductData(id),
                new TypeToken<List<FeedbackDTO>>(){}.getType()
        );
    }
    // TODO: 14.07.2023 convert into request param
    @GetMapping("/client/{id}")
    @PreAuthorize("hasAnyAuthority('admin view info', 'client view info')")
    public List<FeedbackDTO> getAllFeedbacksByClient(@PathVariable Long id) {
        return modelMapper.map(
                feedbackFacade.getAllFeedbacksByClient(id),
                new TypeToken<List<FeedbackDTO>>(){}.getType()
        );
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('feedback management')")
    public ResponseEntity<FeedbackDTO> addNewFeedback(@RequestParam Long clientId,
                                   @RequestBody FeedbackRequestDTO feedbackRequestDto) {
        return ResponseEntity.ok(
                modelMapper.map(
                        feedbackFacade.addFeedback(clientId, feedbackRequestDto),
                        FeedbackDTO.class
                )
        );
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('feedback management')")
    public ResponseEntity<FeedbackDTO> updateFeedback(@RequestBody FeedbackRequestDTO feedbackRequestDTO) {
        return ResponseEntity.ok(
                modelMapper.map(
                        feedbackFacade.updateFeedback(feedbackRequestDTO),
                        FeedbackDTO.class
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
