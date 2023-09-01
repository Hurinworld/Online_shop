package com.serhiihurin.shop.online_shop.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WishlistResponseDTO {
    private Long userId;
    private List<ProductResponseDTO> product;
}
