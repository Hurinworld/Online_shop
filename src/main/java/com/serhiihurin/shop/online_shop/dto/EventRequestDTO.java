package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class EventRequestDTO {
    private String title;
    private String description;
    private LocalDateTime endDateTime;
    //TODO use id while creating entity instead of names
    //TODO use dtos instead of map
    private Map<String, Integer> productsForSale;
}
