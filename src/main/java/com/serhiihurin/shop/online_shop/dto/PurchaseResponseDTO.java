package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseResponseDTO {
    private Long id;
    private LocalDateTime time;
    private List<PurchaseDetailsResponseDTO> details;
}
