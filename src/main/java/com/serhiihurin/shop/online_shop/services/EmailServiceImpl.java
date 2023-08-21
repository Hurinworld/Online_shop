package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.VerificationCodeRepository;
import com.serhiihurin.shop.online_shop.entity.VerificationCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender javaMailSender;
    private final UserService userService;
    private final VerificationCodeRepository verificationCodeRepository;

    @Override
    public void sendGreetingsEmail(String toEmail, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("online.shop@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Welcome!");
        message.setText("Hi "+ name + "!" + "Welcome to the online-shop!");

        javaMailSender.send(message);

        log.info("sent email to {}", toEmail);
    }

    @Override
    public void sendPasswordChangingVerificationCode(String toEmail, String name) {
        VerificationCode verificationCode = VerificationCode.builder()
                .verificationCode(generateVerificationCode())
                .user(userService.getUserByEmail(toEmail))
                .build();
        verificationCodeRepository.save(verificationCode);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("online.shop@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Password changing verification");
        message.setText(
                "Hi "+ name + "! " +
                """
                You are about to change your password.
                To proceed, please, go to this link:
                
                http://localhost:8080/online-shop/info/password/new
                
                and enter your verification code:
                
                """
                + verificationCode.getVerificationCode() +
                        """
                        
                        
                        If it wasn't you, just ignore this message.
                        """
        );

        javaMailSender.send(message);

        log.info("sent password changing verification code to {}", toEmail);
    }

    private String generateVerificationCode() {
        int min = 100000;
        int max = 999999;

        Random random = new Random();
        return String.valueOf(random.nextInt(max - min + 1) + min);
    }
}
