package com.serhiihurin.shop.online_shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsernameUpdateResponseDTO {
    private String newEmail;
    private String accessToken;
    private String refreshToken;
}
