package com.serhiihurin.shop.online_shop.services;

public interface EmailService {
    void sendEmail(String toEmail, String subject, String text);
}
