package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.entity.ProductData;

import java.util.List;

public interface FeedbackService {
    List<Client> getAllFeedbacks();

    List<Feedback> getFeedbacksByProductData(ProductData productData);

    List<Feedback> getFeedbacksByClient(Client client);

    void saveFeedback(Feedback feedback);

    Client getFeedback(Long id);

    void deleteFeedback(Long id);
}
