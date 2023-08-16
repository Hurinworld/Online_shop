package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.FeedbackRepository;
import com.serhiihurin.shop.online_shop.dto.FeedbackUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public List<Feedback> getFeedbacksByProductDataId(Long id) {
        return feedbackRepository.getFeedbacksByProductDataId(id);
    }

    @Override
    public List<Feedback> getFeedbacksByClientId(Long id) {
        return feedbackRepository.getFeedbacksByUserId(id);
    }

    @Override
    public Feedback getFeedback(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Could not find feedback with id" + id));
    }

    @Override
    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback updateFeedback(FeedbackUpdateRequestDTO feedbackUpdateRequestDTO, Feedback feedback) {
        if (feedbackUpdateRequestDTO.getRate() != null) {
            feedback.setRate(feedbackUpdateRequestDTO.getRate());
        }
        if (feedbackUpdateRequestDTO.getText() != null) {
            feedback.setText(feedbackUpdateRequestDTO.getText());
        }
        feedback.setTime(LocalDateTime.now());

        return feedbackRepository.save(feedback);
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }
}
