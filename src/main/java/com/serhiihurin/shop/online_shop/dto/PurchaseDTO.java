package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurchaseDTO {
    private Long id;
    private LocalDateTime time;
}
