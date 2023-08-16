package com.serhiihurin.shop.online_shop.dto;

import com.serhiihurin.shop.online_shop.enums.ProductRate;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackResponseDTO {
    private Long id;
    private String text;
    private LocalDateTime time;
    private ProductRate rate;
    private Long userId;
}
