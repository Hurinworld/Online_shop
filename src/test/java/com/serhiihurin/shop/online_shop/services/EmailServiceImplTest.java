package com.serhiihurin.shop.online_shop.services;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.enums.Role;
import com.serhiihurin.shop.online_shop.services.interfaces.EmailService;
import com.serhiihurin.shop.online_shop.services.interfaces.UserService;
import com.serhiihurin.shop.online_shop.services.interfaces.VerificationCodeService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private UserService userService;
    @Mock
    private VerificationCodeService verificationCodeService;
    @Mock
    private TemplateEngine templateEngine;
    @Mock
    private Context context;
    @InjectMocks
    private EmailServiceImpl emailService;
    private Product testProduct;
    private User testUser;
    private Shop testShop;
    private MimeMessage testMessage;

    private MimeMessageHelper testHelper;

    private static GreenMail greenMail;

    @BeforeAll
    static void setUpGreenMail() {
        ServerSetup serverSetup = new ServerSetup(25, "localhost", "smtp");
        greenMail = new GreenMail(serverSetup);
        greenMail.start();
    }

    @AfterAll
    static void tearDown() {
        greenMail.stop();
    }

    @BeforeEach
    void setUp() throws MessagingException {
        testMessage = javaMailSender.createMimeMessage();
        testHelper = new MimeMessageHelper(testMessage, true, "UTF-8");
        testUser = User.builder()
                .id(1L)
                .firstName("User")
                .lastName("For test")
                .email("testUser@gmail.com")
                .password("password")
                .cash(26665.0)
                .role(Role.SHOP_OWNER)
                .build();
        testShop = Shop.builder()
                .id(1L)
                .name("Test shop")
                .income(325665.0)
                .owner(testUser)
                .build();
        testProduct = Product.builder()
                .id(1L)
                .name("AMD Ryzen 7 5700X")
                .description("CPU")
                .amount(100)
                .price(8500.0)
                .shop(testShop)
                .build();
    }

    @Test
    void sendGreetingsEmail() throws MessagingException {
        //Given
        String subject = "Welcome!";
        //When
        emailService.sendGreetingsEmail(testUser.getEmail(), testUser.getFirstName());
        //Then
        greenMail.waitForIncomingEmail(5000, 1);
        MimeMessage[] mimeMessages = greenMail.getReceivedMessages();
        assertEquals(1, mimeMessages.length);
        assertEquals(testUser.getEmail(), Arrays.toString(mimeMessages[0].getRecipients(Message.RecipientType.TO)));
        assertEquals(subject, mimeMessages[0].getSubject());

    }

    @Test
    void sendPasswordChangingVerificationCode() {
    }

    @Test
    void sendNotificationEmailAboutProductsOnSale() {
    }

    @Test
    void sendNotificationAboutEventStart() {
    }

    @Test
    void testSendGreetingsEmail() {

    }

    @Test
    void testSendPasswordChangingVerificationCode() {
    }

    @Test
    void testSendNotificationEmailAboutProductsOnSale() throws NoSuchMethodException, MessagingException {
        //given
        String templateName = "wishlist-products-on-sale-notification-email";
        String fromEmail = "from@example.com";
        String toEmail = "testUser@gmail.com";
        String subject = "Test Subject";
        String emailContent = "Test Email Content";

        Mockito.when(templateEngine.process(templateName, context)).thenReturn(emailContent);
        testHelper.setFrom(fromEmail);
        testHelper.setTo(toEmail);
        testHelper.setSubject(subject);
        testHelper.setText(emailContent);

        Mockito.when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
        //when
        Method method = EmailServiceImpl.class.getDeclaredMethod(
                "buildAndSendMessage",
                String.class,
                String.class,
                String.class,
                String.class
        );
        method.setAccessible(true);

        emailService.sendNotificationEmailAboutProductsOnSale(toEmail, List.of(testProduct));

        //then
        assertDoesNotThrow(() -> method.invoke(emailService, templateName, fromEmail, toEmail, subject));
        Mockito.verify(javaMailSender).send(testMessage);
        Mockito.verify(templateEngine).process(templateName, context);
    }

    @Test
    void testSendNotificationAboutEventStart() {
    }

    @Test
    void sendNotificationAboutProductAvailability() {
    }

    @Test
    void addToProductAvailabilitySendingQueue() {
    }

    @Test
    void getProductAvailabilitySendingQueue() {
    }
}