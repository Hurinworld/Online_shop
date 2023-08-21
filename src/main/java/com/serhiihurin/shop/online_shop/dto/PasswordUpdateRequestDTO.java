package com.serhiihurin.shop.online_shop.dto;

import lombok.Data;

@Data
public class PasswordUpdateRequestDTO {
    private String verificationCode;
    private String password;
}
