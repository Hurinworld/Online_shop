package com.serhiihurin.shop.online_shop.facades.interfaces;

import com.serhiihurin.shop.online_shop.dto.FeedbackUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.dto.FeedbackRequestDTO;

import java.util.List;

public interface FeedbackFacade {

    List<Feedback> getAllFeedbacks();

    Feedback getFeedback(Long id);

    List<Feedback> getAllFeedbacksByProduct(Long id);

    List<Feedback> getAllFeedbacksByClient(Long id);

    Feedback addFeedback(Long clientId,
                         FeedbackRequestDTO feedbackRequestDTO);

    Feedback updateFeedback(Long id, FeedbackUpdateRequestDTO feedbackUpdateRequestDTO);

    void deleteFeedback(Long id);
}
