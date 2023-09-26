package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
//TODO check is it seems correct in postman response
@Data
public class PurchaseAdminResponseDTO {
    private Long id;
    private LocalDateTime time;
    private List<ProductResponseDTO> products;
    private Long userId;
}
