package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.Feedback;

import java.util.List;

public interface FeedbackFacade {

    List<Feedback> getAllFeedbacks();

    Feedback getFeedback(Long id);

    List<Feedback> getAllFeedbacksByProductData(Long id);

    List<Feedback> getAllFeedbacksByClient(Long id);

    Feedback addFeedback(Long clientId,
                         Long productDataId,
                         Feedback feedback);

    Feedback updateFeedback(Feedback feedback);

    String deleteFeedback(Long id);
}
