package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.NotificationRepository;
import com.serhiihurin.shop.online_shop.entity.Event;
import com.serhiihurin.shop.online_shop.entity.Notification;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public List<Notification> getAllNotificationsByUserId(Long userId) {
        return notificationRepository.findNotificationsByUserId(userId);
    }

    @Override
    public Notification getNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ApiRequestException("Could not find notification with ID: " + notificationId));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public void addWishlistNotification(String userFirstName, List<Product> productList) {
        String title = "Notification about products form wishlist on sale";
        String text = "Hi " + userFirstName + "! "
                + "\n\nSome products from your wishlist are now on sale: \n"
                + productList;
        addNotification(title, text);
    }

    @Override
    public void addEventStartNotification(String userFirstName, Event event) {
        String title = "Notification about " + event.getTitle() + " event start";
        String text = "Hi " + userFirstName + "! \n\n"
                + "Visit Online Shop and checkout new discounts!\n"
                + " Discount will be valid from " + event.getStartDateTime() + " to " + event.getEndDateTime()
                + "\n\nDon't miss the opportunity to get products for nice prices!";
        addNotification(title, text);
    }

    private void addNotification(String title, String text) {
        notificationRepository.save(
                Notification.builder()
                        .title(title)
                        .text(text)
                        .sendDateTime(
                                LocalDateTime.parse(
                                        LocalDateTime.now().atZone(ZoneId.of("Z")).format(formatter),
                                        formatter
                                )
                        )
                        .build()
        );
    }

    @Override
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}
