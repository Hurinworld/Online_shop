package com.serhiihurin.shop.online_shop.services;

public interface EmailService {
    void sendGreetingsEmail(String toEmail, String name);

    void sendPasswordChangingVerificationCode(String toEmail);
}
