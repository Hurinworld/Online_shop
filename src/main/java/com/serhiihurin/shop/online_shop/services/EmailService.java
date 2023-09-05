package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Product;

public interface EmailService {
    void sendGreetingsEmail(String toEmail, String name);

    void sendPasswordChangingVerificationCode(String toEmail);

    void sendNotificationEmailABoutProductsOnSale(String toEmail, Product product);
}
