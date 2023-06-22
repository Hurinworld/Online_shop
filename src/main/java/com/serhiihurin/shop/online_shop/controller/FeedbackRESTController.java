package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.facades.FeedbackFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online_shop/feedbacks")
public class FeedbackRESTController {

    @Autowired
    private FeedbackFacade feedbackFacade;

    @GetMapping
    public List<Feedback> showAllFeedbacks() {
        return feedbackFacade.showAllFeedbacks();
    }

    @GetMapping("/{id}")
    public Feedback showFeedback(@PathVariable Long id) {
        return feedbackFacade.showFeedback(id);
    }

    @GetMapping("/product/{id}")
    public List<Feedback> showAllFeedbacksByProductData(@PathVariable Long id) {
        return feedbackFacade.showAllFeedbacksByProductData(id);
    }

    @GetMapping("/client/{id}")
    public List<Feedback> showAllFeedbacksByClient(@PathVariable Long id) {
        return feedbackFacade.showAllFeedbacksByClient(id);
    }

    @PostMapping("/{clientId}/{productDataId}")
    public Feedback addNewFeedback(@PathVariable Long clientId,
                                   @PathVariable Long productDataId,
                                   @RequestBody Feedback feedback) {
        return feedbackFacade.addFeedback(clientId, productDataId, feedback);
    }

    @PatchMapping
    public Feedback updateFeedback(@RequestBody Feedback feedback) {
        return feedbackFacade.updateFeedback(feedback);
    }

    @DeleteMapping("/{id}")
    public String deleteFeedback(@PathVariable Long id) {
        return feedbackFacade.deleteFeedback(id);
    }
}
