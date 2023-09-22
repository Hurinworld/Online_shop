package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationPreviewResponseDTO {
    private String title;
    private LocalDateTime sendDateTime;
}
