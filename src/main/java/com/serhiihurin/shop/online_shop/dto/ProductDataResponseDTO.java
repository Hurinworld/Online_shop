package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

@Data
public class ProductDataResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long ShopId;
}
