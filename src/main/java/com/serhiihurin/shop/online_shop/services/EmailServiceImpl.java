package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Event;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.VerificationCode;
import com.serhiihurin.shop.online_shop.services.interfaces.EmailService;
import com.serhiihurin.shop.online_shop.services.interfaces.UserService;
import com.serhiihurin.shop.online_shop.services.interfaces.VerificationCodeService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    //TODO check it for an architectures issues //done
    private final UserService userService;
    private final VerificationCodeService verificationCodeService;
    private final TemplateEngine templateEngine;
    private final Context context;

    @Value("${custom.password-changing-link}")
    private String passwordChangingLink;
    @Value("${custom.wishlist-products-on-sale-notification-link}")
    private String productRetrievingLink;
    @Value("${custom.sender-email}")
    private String fromEmail;

    @Getter
    @Setter
    private Map<String, List<Product>> productAvailabilitySendingQueue = new HashMap<>();

    @Async
    @Override
    public void sendGreetingsEmail(String toEmail, String name) {
        context.setVariable("name", name);

        buildAndSendMessage("greetings-email", fromEmail, toEmail, "Welcome!");

        log.info("sent email to {}", toEmail);
    }

    @Async
    @Override
    public void sendPasswordChangingVerificationCode(String toEmail) {
        VerificationCode verificationCode = verificationCodeService.createVerificationCode(
                userService.getUserByEmail(toEmail)
        );

        context.setVariable("name", userService.getUserByEmail(toEmail).getFirstName());
        context.setVariable("verificationCode", verificationCode.getVerificationCode());
        context.setVariable("passwordChangingLink", passwordChangingLink);

        buildAndSendMessage("password-changing-verification-email", fromEmail,
                toEmail, "Password changing verification");

        log.info("sent password changing verification code to {}", toEmail);
    }

    @Async
    @Override
    public void sendNotificationEmailAboutProductsOnSale(String toEmail, List<Product> products) {
        context.setVariable("name", userService.getUserByEmail(toEmail).getFirstName());
        context.setVariable("products", products);
        context.setVariable("productRetrievingLink", productRetrievingLink);

        buildAndSendMessage(
                "wishlist-products-on-sale-notification-email", fromEmail,
                toEmail, "Notification about discount on products from your wishlist"
        );
    }

    @Async
    @Override
    public void sendNotificationAboutEventStart(String toEmail, Event event) {
        context.setVariable("eventTitle", event.getTitle());
        context.setVariable("startDateTime", event.getStartDateTime());
        context.setVariable("endDateTime", event.getEndDateTime());

        buildAndSendMessage("event-starting-notification", fromEmail,
                toEmail, "Notification about event start");

        log.info("Sent notification about starting event with id {} at {}",
                event.getId(), LocalDateTime.now().atZone(ZoneId.of("Z")));
    }

    @Async
    @Override
    public void sendNotificationAboutProductAvailability(String toEmail, List<Product> products) {
        context.setVariable("name", userService.getUserByEmail(toEmail).getFirstName());
        context.setVariable("productRetrievingLink", productRetrievingLink);
        context.setVariable("products", products);

        log.info("sent email");

        buildAndSendMessage("product-availability-notification", fromEmail,
                toEmail, "Notification about product availability");
    }

    @Override
    public void addToProductAvailabilitySendingQueue(String email, Product product) {
        List<Product> products = productAvailabilitySendingQueue.getOrDefault(email, new ArrayList<>());
        products.add(product);
        productAvailabilitySendingQueue.put(email, products);
    }

    private void buildAndSendMessage(
            String templateName,
            String fromEmail,
            String toEmail,
            String subject
    ) {
        String emailContent = templateEngine.process(templateName, context);
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(emailContent, true);
            context.clearVariables();
        } catch (MessagingException messagingException) {
            throw new RuntimeException(messagingException);
        }

        javaMailSender.send(message);
    }
}
