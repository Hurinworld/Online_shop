package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseAdminResponseDTO {
    private Long id;
    private LocalDateTime time;
    private List<ProductResponseDTO> products;
    private Long userId;
}
