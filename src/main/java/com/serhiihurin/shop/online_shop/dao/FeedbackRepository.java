package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.Client;
import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> getFeedbacksByProductData(ProductData productData);
    List<Feedback> getFeedbacksByClient(Client client);
}
