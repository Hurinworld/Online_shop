package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

@Data
public class ProductForSaleRequestDTO {
    private Long productId;
    private Integer discountPercent;
}
