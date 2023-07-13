package com.serhiihurin.shop.online_shop.dto;

import com.serhiihurin.shop.online_shop.entity.ProductData;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private boolean isBought;
    private ProductData productData;
}
