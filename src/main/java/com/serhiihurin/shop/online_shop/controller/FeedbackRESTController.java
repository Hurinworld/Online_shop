package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.facades.FeedbackFacade;
import com.serhiihurin.shop.online_shop.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online_shop/feedbacks")
public class FeedbackRESTController {

    @Autowired
    private FeedbackFacade feedbackFacade;


    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackFacade.getAllFeedbacks();
    }

    @GetMapping("/{id}")
    public Feedback getFeedback(@PathVariable Long id) {
        return feedbackFacade.getFeedback(id);
    }

    @GetMapping("/product_data/{id}")
    public List<Feedback> getAllFeedbacksByProductData(@PathVariable Long id) {
        return feedbackFacade.getAllFeedbacksByProductData(id);
    }

    @GetMapping("/client/{id}")
    public List<Feedback> getAllFeedbacksByClient(@PathVariable Long id) {
        return feedbackFacade.getAllFeedbacksByClient(id);
    }

    @PostMapping()
    public Feedback addNewFeedback(@RequestParam Long clientId,
                                   @RequestParam Long productDataId,
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
