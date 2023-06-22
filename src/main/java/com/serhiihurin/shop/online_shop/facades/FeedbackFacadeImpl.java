package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.services.ClientService;
import com.serhiihurin.shop.online_shop.services.FeedbackService;
import com.serhiihurin.shop.online_shop.services.ProductDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class FeedbackFacadeImpl implements FeedbackFacade{

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductDataService productDataService;

    @Override
    public List<Feedback> showAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @Override
    public Feedback showFeedback(Long id) {
        return feedbackService.getFeedback(id);
    }

    @Override
    public List<Feedback> showAllFeedbacksByProductData(Long id) {
        return feedbackService.getFeedbacksByProductDataId(id);
    }

    @Override
    public List<Feedback> showAllFeedbacksByClient(Long id) {
        return feedbackService.getFeedbacksByClientId(id);
    }

    @Override
    public Feedback addFeedback(Long clientId, Long productDataId, Feedback feedback) {
        Client client = clientService.getClient(clientId);
        ProductData productData = productDataService.getProductData(productDataId);

        feedback.setTime(LocalDateTime.now());
        feedback.setClient(client);
        feedback.setProductData(productData);

        feedbackService.saveFeedback(feedback);

        return feedback;
    }

    @Override
    public Feedback updateFeedback(Feedback feedback) {
        Feedback oldFeedback = feedbackService.getFeedback(feedback.getId());
        Client client = oldFeedback.getClient();
        ProductData productData = oldFeedback.getProductData();

        feedback.setClient(client);
        feedback.setProductData(productData);
        feedback.setTime(LocalDateTime.now());

        feedbackService.saveFeedback(feedback);
        return feedback;
    }

    @Override
    public String deleteFeedback(Long id) {
        feedbackService.deleteFeedback(id);
        return "Feedback with id = " + id + " was deleted";
    }
}
