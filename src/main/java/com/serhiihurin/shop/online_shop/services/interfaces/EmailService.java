package com.serhiihurin.shop.online_shop.services.interfaces;

import com.serhiihurin.shop.online_shop.entity.Event;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;

import java.util.List;
import java.util.Map;

public interface EmailService {
    void sendGreetingsEmail(String toEmail, String name);

    void sendPasswordChangingVerificationCode(String toEmail);

    void sendNotificationEmailAboutProductsOnSale(String toEmail, List<Product> products);

    void sendNotificationAboutEventStart(String toEmail, Event event);

    void sendNotificationAboutProductAvailability(String toEmail, List<Product> products);

    Map<String, List<Product>> getProductAvailabilitySendingQueue();
    void setProductAvailabilitySendingQueue(Map<String, List<Product>> productAvailabilitySendingQueue);

    void addToProductAvailabilitySendingQueue(String email, Product product);
}
