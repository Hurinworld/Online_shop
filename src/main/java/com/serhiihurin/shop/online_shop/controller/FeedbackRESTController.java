package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.facades.FeedbackFacade;
import com.serhiihurin.shop.online_shop.form.FeedbackForm;
import lombok.RequiredArgsConstructor;
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


    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public List<Feedback> getAllFeedbacks() {
        return feedbackFacade.getAllFeedbacks();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public Feedback getFeedback(@PathVariable Long id) {
        return feedbackFacade.getFeedback(id);
    }

    @GetMapping("/product-data/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'client:read')")
    public List<Feedback> getAllFeedbacksByProductData(@PathVariable Long id) {
        return feedbackFacade.getAllFeedbacksByProductData(id);
    }

    @GetMapping("/client/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'client:read')")
    public List<Feedback> getAllFeedbacksByClient(@PathVariable Long id) {
        return feedbackFacade.getAllFeedbacksByClient(id);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('client:create')")
    public ResponseEntity<Feedback> addNewFeedback(@RequestParam Long clientId,
                                   @RequestParam Long productDataId,
                                   @RequestBody FeedbackForm feedbackForm) {
        return ResponseEntity.ok(feedbackFacade.addFeedback(clientId, productDataId, feedbackForm));
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('client:update')")
    public ResponseEntity<Feedback> updateFeedback(@RequestBody Feedback feedback) {
        return ResponseEntity.ok(feedbackFacade.updateFeedback(feedback));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'client:delete')")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackFacade.deleteFeedback(id);
        return ResponseEntity.ok().build();
    }
}
