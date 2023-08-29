package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "verification_codes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VerificationCode {
    @Id
    private String verificationCode;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    //TODO add field createdDate + make new variable in application.yml for duration of this code
}
