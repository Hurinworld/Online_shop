package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationPreviewResponseDTO {
    //TODO add id //done
    private Long id;
    private String title;
    private LocalDateTime sendDateTime;
}
