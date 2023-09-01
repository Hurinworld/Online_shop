package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

@Data
public class PurchaseDetailsResponseDTO {
    private Long productId;
    private Integer amount;
    private Double totalPrice;
}
