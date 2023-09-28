package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.NotificationRepository;
import com.serhiihurin.shop.online_shop.entity.Notification;
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
    public void addNotification(String title, String text) {
        notificationRepository.save(
                Notification.builder()
                        .title(title)
                        .text(text)
                        .sendDateTime(
                                LocalDateTime.parse(
                                        LocalDateTime.now().atZone(ZoneId.of("Z")).format(formatter)
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
