package com.serhiihurin.shop.online_shop.dto;

import com.serhiihurin.shop.online_shop.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseForAdminDTO {
    private Long id;
    //TODO rename to imageUrls
    List<String> imagesEndpoints;
    private String firstName;
    private String lastName;
    private Double cash;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
}
