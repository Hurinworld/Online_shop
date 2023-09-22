package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.NotificationFullViewResponseDTO;
import com.serhiihurin.shop.online_shop.dto.NotificationPreviewResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.services.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/notifications")
@Tag(name = "Notifications")
@RequiredArgsConstructor
public class NotificationRESTController {
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<NotificationPreviewResponseDTO> getAllNotificationsByUser(User currentAuthenticatedUser) {
        return modelMapper.map(
                notificationService.getAllNotificationsByUserId(currentAuthenticatedUser.getId()),
                new TypeToken<List<NotificationPreviewResponseDTO>>() {
                }.getType()
        );
    }

    @GetMapping("/{notificationId}")
    public NotificationFullViewResponseDTO getNotification(@PathVariable Long notificationId) {
        return modelMapper.map(
                notificationService.getNotification(notificationId),
                NotificationFullViewResponseDTO.class
        );
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok().build();
    }
}
