package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dto.FeedbackUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    List<Feedback> getAllFeedbacks();

    List<Feedback> getFeedbacksByProductDataId(Long id);

    List<Feedback> getFeedbacksByClientId(Long id);

    Feedback getFeedback(Long id);

    Feedback saveFeedback(Feedback feedback);

    Feedback updateFeedback(FeedbackUpdateRequestDTO feedbackUpdateRequestDTO, Feedback feedback);

    void deleteFeedback(Long id);
}
