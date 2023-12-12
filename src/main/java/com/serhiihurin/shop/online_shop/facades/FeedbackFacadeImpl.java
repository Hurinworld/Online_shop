package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.FeedbackUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.dto.FeedbackRequestDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.facades.interfaces.FeedbackFacade;
import com.serhiihurin.shop.online_shop.services.interfaces.UserService;
import com.serhiihurin.shop.online_shop.services.interfaces.FeedbackService;
import com.serhiihurin.shop.online_shop.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedbackFacadeImpl implements FeedbackFacade {
    private final FeedbackService feedbackService;
    private final UserService userService;
    private final ProductService productService;

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @Override
    public Feedback getFeedback(Long id) {
        return feedbackService.getFeedback(id);
    }

    @Override
    public List<Feedback> getAllFeedbacksByProduct(Long id) {
        if (productService.getProduct(id) == null) {
            throw new ApiRequestException("Could not find feedbacks of this product");
        }
        return feedbackService.getFeedbacksByProductId(id);
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
        User user = userService.getUser(clientId);
        Product product = productService.getProduct(feedbackRequestDto.getProductId());

        return feedbackService.saveFeedback(
                Feedback.builder()
                        .text(feedbackRequestDto.getText())
                        .rate(feedbackRequestDto.getRate())
                        .time(LocalDateTime.now())
                        .user(user)
                        .product(product)
                        .build()
        );
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
