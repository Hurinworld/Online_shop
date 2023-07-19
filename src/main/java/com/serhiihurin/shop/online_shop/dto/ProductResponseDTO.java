package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

@Data
public class ProductResponseDTO {
    private Long id;
    private boolean isBought;
    private ProductDataResponseDTO productDataResponseDTO;
}
