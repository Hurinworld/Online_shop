package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> getAllNotificationsByUserId(Long userId);

    Notification getNotification(Long notificationId);

    void addNotification(String title, String text);

    void deleteNotification(Long notificationId);
}
