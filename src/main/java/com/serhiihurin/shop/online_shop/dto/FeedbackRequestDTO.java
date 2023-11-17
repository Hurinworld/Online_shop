package com.serhiihurin.shop.online_shop.dto;

import com.serhiihurin.shop.online_shop.enums.ProductRate;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
public class FeedbackRequestDTO {
    private Long productId;
    private String text;
    @Enumerated(EnumType.STRING)
    private ProductRate rate;
}
