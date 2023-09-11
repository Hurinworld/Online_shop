package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

@Data
public class ProductRequestDTO {
    private Long shopId;
    private String name;
    private String description;
    private Double price;
    private Integer amount;
}
