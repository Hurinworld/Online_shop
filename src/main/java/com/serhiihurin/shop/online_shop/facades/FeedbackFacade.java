package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.dto.FeedbackRequestDTO;

import java.util.List;

public interface FeedbackFacade {

    List<Feedback> getAllFeedbacks();

    Feedback getFeedback(Long id);

    List<Feedback> getAllFeedbacksByProductData(Long id);

    List<Feedback> getAllFeedbacksByClient(Long id);

    Feedback addFeedback(Long clientId,
                         FeedbackRequestDTO feedbackform);

    Feedback updateFeedback(Feedback feedback);

    void deleteFeedback(Long id);
}
