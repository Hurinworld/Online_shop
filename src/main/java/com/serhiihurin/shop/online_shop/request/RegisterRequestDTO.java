package com.serhiihurin.shop.online_shop.request;

import com.serhiihurin.shop.online_shop.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    private String firstName;
    private String lastName;
    private Double cash;
    private String email;
    private String password;
    private Role role;
}
