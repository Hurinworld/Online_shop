package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.services.ClientService;
import com.serhiihurin.shop.online_shop.services.FeedbackService;
import com.serhiihurin.shop.online_shop.services.ProductDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/online_shop/feedbacks")
public class FeedbackRESTController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductDataService productDataService;

    private Client client;
    private ProductData productData;

    @GetMapping
    public List<Feedback> showAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("/{id}")
    public Feedback showFeedback(@PathVariable Long id) {
        return feedbackService.getFeedback(id);
    }

    @GetMapping("/product/{id}")
    public List<Feedback> showAllFeedbacksByProductData(@PathVariable Long id) {
        return feedbackService.getFeedbacksByProductDataId(id);
    }

    @GetMapping("/client/{id}")
    public List<Feedback> showAllFeedbacksByClient(@PathVariable Long id) {
        return feedbackService.getFeedbacksByClientId(id);
    }

    @PostMapping("/{clientId}/{productDataId}")
    public Feedback addNewFeedback(@PathVariable Long clientId, @PathVariable Long productDataId,
                                   @RequestBody Feedback feedback) {
        client = clientService.getClient(clientId);
        productData = productDataService.getProductData(productDataId);

        feedback.setTime(LocalDateTime.now());
        feedback.setClient(client);
        feedback.setProductData(productData);

        feedbackService.saveFeedback(feedback);
        return feedback;
    }

    @PatchMapping
    public Feedback updateFeedback(@RequestBody Feedback feedback) {
        feedback.setTime(LocalDateTime.now());
        feedback.setClient(client);
        feedback.setProductData(productData);

        feedbackService.saveFeedback(feedback);
        return feedback;
    }

    @DeleteMapping("/{id}")
    public String deleteProductData(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return "Feedback with id = " + id + " was deleted";
    }
}
