package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductResponseDTO {
    private Long id;
    private String name;
    private List<String> imagesPaths;
    private List<byte[]> images;
    private String description;
    private Double price;
    private Long ShopId;
    private Double rate;

    //TODO add average rating for this products by feedbacks //done
}
