package com.serhiihurin.shop.online_shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    //TODO rename to imageUrls
    List<String> imagesEndpoints;
    private String firstName;
    private String lastName;
    private String email;
}
