package com.serhiihurin.shop.online_shop.form;

import com.serhiihurin.shop.online_shop.enums.ProductRate;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class FeedbackRequestDTO {
    private Long productDataId;
    private String text;
    @Enumerated(EnumType.STRING)
    private ProductRate rate;
}
