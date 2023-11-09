package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductResponseDTO {
    private Long id;
    private String name;
    private List<String> imagesEndpoints;
    private String description;
    private Double price;
    private Long ShopId;
    private Double rate;
}
