package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.NotificationRepository;
import com.serhiihurin.shop.online_shop.entity.Event;
import com.serhiihurin.shop.online_shop.entity.Notification;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.enums.Role;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.services.interfaces.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {
    @Mock
    private NotificationRepository notificationRepository;
    private NotificationService notificationService;
    private Notification testNotification;
    private Event testEvent;
    private User testUser;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setUp() {
        notificationService = new NotificationServiceImpl(notificationRepository);
        testUser = User.builder()
                .id(1L)
                .firstName("User")
                .lastName("For test")
                .email("testUser@gmail.com")
                .password("password")
                .cash(26665.0)
                .role(Role.CLIENT)
                .build();
        testEvent = Event.builder()
                .id(1L)
                .title("Test event")
                .description("Test event description")
                .eventCreator(testUser)
                .startDateTime(LocalDateTime.parse(
                                LocalDateTime.now().format(formatter), formatter
                        )
                )
                .endDateTime(
                        LocalDateTime.parse(
                                LocalDateTime.now().format(formatter), formatter
                        ).plusHours(1)
                )
                .build();
        testNotification = Notification.builder()
                .id(1L)
                .title("Test notification")
                .text("Test notification text")
                .sendDateTime(
                        LocalDateTime.parse(
                        LocalDateTime.now().atZone(ZoneId.of("Z")).format(formatter), formatter
                    )
                )
                .isRead(false)
                .user(testUser)
                .build();
    }

    @Test
    void getAllNotificationsByUserId() {
        // given
        List<Notification> expectedNotifications = List.of(testNotification);

        Mockito.when(notificationRepository.findNotificationsByUserId(testUser.getId()))
                .thenReturn(expectedNotifications);
        // when
        List<Notification> actualNotifications = notificationService.getAllNotificationsByUserId(testUser.getId());
        // then
        assertThat(expectedNotifications).isEqualTo(actualNotifications);
        Mockito.verify(notificationRepository).findNotificationsByUserId(testUser.getId());
    }

    @Test
    void getNotification() {
        //given
        Mockito.when(notificationRepository.findById(testNotification.getId()))
                .thenReturn(Optional.of(testNotification));
        Mockito.when(notificationRepository.save(testNotification)).thenReturn(testNotification);
        //when
        Notification capturedValue = notificationService.getNotification(testNotification.getId());
        //then
        assertThat(capturedValue).isEqualTo(testNotification);
        assertTrue(capturedValue.isRead());
        Mockito.verify(notificationRepository).save(testNotification);
    }

    @Test
    public void getNotification_WithNonExistingId_ShouldThrowException() {
        // given
        Long wrongID = 999L;
        //when
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> notificationService.getNotification(wrongID));
        // then
        assertThat(exception.getMessage()).isEqualTo("Could not find notification with ID: " + wrongID);
    }

    @Test
    void addWishlistNotification() {
        //given
        List<Product> productList = List.of(new Product());
        //when
        notificationService.addWishlistNotification(testUser.getFirstName(), productList);

        //then
        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        Mockito.verify(notificationRepository).save(notificationCaptor.capture());

        Notification capturedNotification = notificationCaptor.getValue();

        assertNotNull(capturedNotification);
        assertEquals("Notification about products form wishlist on sale",
                capturedNotification.getTitle());
        assertTrue(capturedNotification.getText().contains("Hi " + testUser.getFirstName() + "!"));
        assertTrue(capturedNotification.getText().contains("\n\nSome products from your wishlist are now on sale: \n"));
        assertTrue(capturedNotification.getText().contains(productList.toString()));
    }

    @Test
    void addEventStartNotification() {
        //when
        notificationService.addEventStartNotification(testUser.getFirstName(), testEvent);

        //then
        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        Mockito.verify(notificationRepository).save(notificationCaptor.capture());

        Notification capturedNotification = notificationCaptor.getValue();

        assertNotNull(capturedNotification);
        assertEquals("Notification about " + testEvent.getTitle() + " event start",
                capturedNotification.getTitle());
        assertTrue(capturedNotification.getText().contains("Hi " + testUser.getFirstName() + "!"));
        assertTrue(capturedNotification.getText().contains("Visit Online Shop and checkout new discounts!"));
        assertTrue(capturedNotification.getText().contains(
                "Discount will be valid from "+ testEvent.getStartDateTime() + " to " + testEvent.getEndDateTime())
        );
        assertTrue(capturedNotification.getText()
                .contains("Don't miss the opportunity to get products for nice prices!"));
    }

    @Test
    void addNotification() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        testNotification.setId(null);
        Method addNotificationMethod =
                NotificationServiceImpl.class.getDeclaredMethod("addNotification", String.class, String.class);
        addNotificationMethod.setAccessible(true);

        //when
        addNotificationMethod.invoke(notificationService, testNotification.getTitle(), testNotification.getText());

        //then
        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        Mockito.verify(notificationRepository).save(notificationCaptor.capture());

        Notification capturedNotification = notificationCaptor.getValue();
        assertNotNull(capturedNotification);
        assertEquals(testNotification.getTitle(), capturedNotification.getTitle());
        assertEquals(testNotification.getText(), capturedNotification.getText());
        assertNotNull(capturedNotification.getSendDateTime());
    }

    @Test
    void deleteNotification() {
        //when
        notificationService.deleteNotification(testNotification.getId());
        //then
        Mockito.verify(notificationRepository).deleteById(testNotification.getId());
    }
}