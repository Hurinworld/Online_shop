package com.serhiihurin.shop.online_shop.controller.interfaces;

import com.serhiihurin.shop.online_shop.dto.NotificationFullViewResponseDTO;
import com.serhiihurin.shop.online_shop.dto.NotificationPreviewResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface NotificationController {
    List<NotificationPreviewResponseDTO> getAllNotificationsByUser(User currentAuthenticatedUser);

    NotificationFullViewResponseDTO getNotification(@PathVariable Long notificationId);

    ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId);
}
