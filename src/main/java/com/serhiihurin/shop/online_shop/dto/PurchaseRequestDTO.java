package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

import java.util.Map;

@Data
public class PurchaseRequestDTO {
    private Map<Long, Integer> productsCount;
}
