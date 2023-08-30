package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.VerificationCodeRepository;
import com.serhiihurin.shop.online_shop.entity.VerificationCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender javaMailSender;
    private final UserService userService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final TemplateEngine templateEngine;
    private final Context context;

    @Override
    public void sendGreetingsEmail(String toEmail, String name) {
        context.setVariable("name", userService.getUserByEmail(toEmail).getFirstName());
        String emailContent = templateEngine.process("greetings-email", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("online.shop@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Welcome!");
            helper.setText(emailContent, true);
            context.clearVariables();
        } catch (MessagingException messagingException) {
            throw new RuntimeException(messagingException);
        }

        javaMailSender.send(message);

        log.info("sent email to {}", toEmail);
    }

    @Override
    public void sendPasswordChangingVerificationCode(String toEmail) {
        VerificationCode verificationCode = VerificationCode.builder()
                .verificationCode(generateVerificationCode())
                .user(userService.getUserByEmail(toEmail))
                .creationTime(LocalDateTime.now())
                .build();
        verificationCodeRepository.save(verificationCode);

        context.setVariable("name", userService.getUserByEmail(toEmail).getFirstName());
        context.setVariable("verificationCode", verificationCode.getVerificationCode());
        String emailContent = templateEngine.process("password-changing-verification-email", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("online.shop@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Password changing verification");
            helper.setText(emailContent, true);
            context.clearVariables();
        } catch (MessagingException messagingException) {
            throw new RuntimeException(messagingException);
        }

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
