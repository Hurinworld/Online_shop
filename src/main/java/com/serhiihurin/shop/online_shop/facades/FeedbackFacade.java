package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.Feedback;

import java.util.List;

public interface FeedbackFacade {

    List<Feedback> showAllFeedbacks();
    Feedback showFeedback(Long id);
    List<Feedback> showAllFeedbacksByProductData(Long id);
    List<Feedback> showAllFeedbacksByClient(Long id);
    Feedback addFeedback(Long clientId,
                         Long productDataId,
                         Feedback feedback);
    Feedback updateFeedback(Feedback feedback);
    String deleteFeedback(Long id);
}
