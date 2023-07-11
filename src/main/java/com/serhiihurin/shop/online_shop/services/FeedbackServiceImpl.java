package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.FeedbackRepository;
import com.serhiihurin.shop.online_shop.entity.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

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
    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback getFeedback(Long id) {
        Feedback feedback = null;
        Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
        if (optionalFeedback.isPresent()) {
            feedback = optionalFeedback.get();
        }
        return feedback;
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }
}
