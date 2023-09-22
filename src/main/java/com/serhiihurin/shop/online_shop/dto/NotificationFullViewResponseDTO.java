package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationFullViewResponseDTO {
    private Long id;
    private String title;
    private String text;
    private LocalDateTime sendDateTime;
}
