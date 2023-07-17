package com.serhiihurin.shop.online_shop.dto;

import com.serhiihurin.shop.online_shop.enums.ProductRate;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class FeedbackRequestDTO {
    private Long productDataId;
    private Long feedbackId;
    private String text;
    @Enumerated(EnumType.STRING)
    private ProductRate rate;
}
