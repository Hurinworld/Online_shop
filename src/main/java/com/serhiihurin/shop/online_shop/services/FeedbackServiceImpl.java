package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.FeedbackRepository;
import com.serhiihurin.shop.online_shop.dto.FeedbackRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        return feedbackRepository.getFeedbacksByClientId(id);
    }

    @Override
    public Feedback getFeedback(Long id) {
        Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
        if (optionalFeedback.isEmpty()) {
            throw new ApiRequestException("Could not find feedback with id" + id);
        }
        return optionalFeedback.get();
    }

    @Override
    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback updateFeedback(FeedbackRequestDTO feedbackRequestDTO, Feedback feedback) {
        if (feedbackRequestDTO.getRate() != null) {
            feedback.setRate(feedbackRequestDTO.getRate());
        }
        if (feedbackRequestDTO.getText() != null) {
            feedback.setText(feedbackRequestDTO.getText());
        }
        feedback.setTime(LocalDateTime.now());

        return feedbackRepository.save(feedback);
    }


    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }
}
