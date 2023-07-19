package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

@Data
public class ProductDataRequestDTO {
    private Long id;
    private Long shopId;
    private String name;
    private String description;
    private Double price;
    private int count;
}
