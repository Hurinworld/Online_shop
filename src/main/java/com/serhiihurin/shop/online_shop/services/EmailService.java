package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Product;

import java.util.List;

public interface EmailService {
    void sendGreetingsEmail(String toEmail, String name);

    void sendPasswordChangingVerificationCode(String toEmail);

    //TODO mistake in case-naming //done
    void sendNotificationEmailAboutProductsOnSale(String toEmail, List<Product> products);
}
