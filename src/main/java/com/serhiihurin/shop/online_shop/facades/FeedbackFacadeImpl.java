package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.FeedbackUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.dto.FeedbackRequestDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.services.UserService;
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
    private final UserService userService;
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
        if (productDataService.getProductData(id) == null) {
            throw new ApiRequestException("Could not find feedbacks of this product");
        }
        return feedbackService.getFeedbacksByProductDataId(id);
    }

    @Override
    public List<Feedback> getAllFeedbacksByClient(Long id) {
        if (userService.getUser(id) == null) {
            throw new ApiRequestException("Could not find feedbacks of this client");
        }
        return feedbackService.getFeedbacksByClientId(id);
    }

    @Override
    public Feedback addFeedback(Long clientId, FeedbackRequestDTO feedbackRequestDto) {
        Feedback feedback = new Feedback(feedbackRequestDto.getText(), feedbackRequestDto.getRate());
        User user = userService.getUser(clientId);
        ProductData productData = productDataService.getProductData(feedbackRequestDto.getProductDataId());

        feedback.setTime(LocalDateTime.now());
        feedback.setUser(user);
        feedback.setProductData(productData);

        return feedbackService.saveFeedback(feedback);
    }

    @Override
    public Feedback updateFeedback(Long id, FeedbackUpdateRequestDTO feedbackUpdateRequestDTO) {
        Feedback oldFeedback = feedbackService.getFeedback(id);
        return feedbackService.updateFeedback(feedbackUpdateRequestDTO, oldFeedback);
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackService.deleteFeedback(id);
    }
}
