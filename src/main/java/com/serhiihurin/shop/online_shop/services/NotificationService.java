package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Event;
import com.serhiihurin.shop.online_shop.entity.Notification;
import com.serhiihurin.shop.online_shop.entity.Product;

import java.util.List;

public interface NotificationService {
    List<Notification> getAllNotificationsByUserId(Long userId);

    Notification getNotification(Long notificationId);

    void addWishlistNotification(String userFirstName, List<Product> productList);

    void addEventStartNotification(String userFirstName, Event event);

//    void addNotification(String title, String text);

    void deleteNotification(Long notificationId);
}
