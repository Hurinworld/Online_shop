package com.serhiihurin.shop.online_shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Double cash;
    private String email;
    private String password;
}
