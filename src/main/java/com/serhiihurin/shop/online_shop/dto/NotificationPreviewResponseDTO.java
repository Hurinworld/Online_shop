package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationPreviewResponseDTO {
    //TODO add id
    private String title;
    private LocalDateTime sendDateTime;
}
