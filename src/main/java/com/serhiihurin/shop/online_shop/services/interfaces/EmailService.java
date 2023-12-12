package com.serhiihurin.shop.online_shop.services.interfaces;

import com.serhiihurin.shop.online_shop.entity.Event;
import com.serhiihurin.shop.online_shop.entity.Product;

import java.util.List;
import java.util.Map;

public interface EmailService {
    void sendGreetingsEmail(String toEmail, String name);

    void sendPasswordChangingVerificationCode(String toEmail);

    void sendNotificationEmailAboutProductsOnSale(String toEmail, List<Product> products);

    void sendNotificationAboutEventStart(String toEmail, Event event);

    void sendNotificationAboutProductAvailability(String toEmail, Product product);

    Map<String, List<Product>> getProductAvailabilitySendingQueue();
}
