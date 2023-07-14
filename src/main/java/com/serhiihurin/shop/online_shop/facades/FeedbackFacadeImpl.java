package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.dto.FeedbackRequestDTO;
import com.serhiihurin.shop.online_shop.services.ClientService;
import com.serhiihurin.shop.online_shop.services.FeedbackService;
import com.serhiihurin.shop.online_shop.services.ProductDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedbackFacadeImpl implements FeedbackFacade {
    private final FeedbackService feedbackService;
    private final ClientService clientService;
    private final ProductDataService productDataService;

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @Override
    public Feedback getFeedback(Long id) {
        return feedbackService.getFeedback(id);
    }

    @Override
    public List<Feedback> getAllFeedbacksByProductData(Long id) {
        return feedbackService.getFeedbacksByProductDataId(id);
    }

    @Override
    public List<Feedback> getAllFeedbacksByClient(Long id) {
        return feedbackService.getFeedbacksByClientId(id);
    }

    @Override
    public Feedback addFeedback(Long clientId, FeedbackRequestDTO feedbackRequestDto) {
        Feedback feedback = new Feedback(feedbackRequestDto.getText(), feedbackRequestDto.getRate());
        Client client = clientService.getClient(clientId);
        ProductData productData = productDataService.getProductData(feedbackRequestDto.getProductDataId());

        feedback.setTime(LocalDateTime.now());
        feedback.setClient(client);
        feedback.setProductData(productData);

        return feedbackService.saveFeedback(feedback);
    }

    @Override
    public Feedback updateFeedback(Feedback feedback) {
        Feedback oldFeedback = feedbackService.getFeedback(feedback.getId());
        Client client = oldFeedback.getClient();
        ProductData productData = oldFeedback.getProductData();

        feedback.setClient(client);
        feedback.setProductData(productData);
        feedback.setTime(LocalDateTime.now());

        return feedbackService.saveFeedback(feedback);
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackService.deleteFeedback(id);
    }
}
