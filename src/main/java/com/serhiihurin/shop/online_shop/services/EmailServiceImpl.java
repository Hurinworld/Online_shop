package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.VerificationCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender javaMailSender;
    private final UserService userService;
    private final VerificationCodeService verificationCodeService;
    private final TemplateEngine templateEngine;
    private final Context context;

    @Value("${custom.password-changing-link}")
    private String passwordChangingLink;

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
        //TODO move verificationCode creation to verificationCode service //done
        //TODO add checking for an existing codes before creation a new one //done
        VerificationCode verificationCode = verificationCodeService.createVerificationCode(
                userService.getUserByEmail(toEmail)
        );

        context.setVariable("name", userService.getUserByEmail(toEmail).getFirstName());
        context.setVariable("verificationCode", verificationCode.getVerificationCode());
        context.setVariable("passwordChangingLink", passwordChangingLink);
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

    //TODO I want only 1 email about discounts everyday //done
    @Override
    public void sendNotificationEmailAboutProductsOnSale(String toEmail, List<Product> products) {
        context.setVariable("name", userService.getUserByEmail(toEmail).getFirstName());
        context.setVariable("products", products);

        String emailContent = templateEngine.process("wishlist-products-on-sale-notification-email", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("online.shop@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Notification about discount on products from your wishlist");
            helper.setText(emailContent, true);
            context.clearVariables();
        } catch (MessagingException messagingException) {
            throw new RuntimeException(messagingException);
        }

        javaMailSender.send(message);
    }
}
