package com.serhiihurin.shop.online_shop.entity;

import com.serhiihurin.shop.online_shop.converters.ProductRateConverter;
import com.serhiihurin.shop.online_shop.enums.ProductRate;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private LocalDateTime time;
//    @Enumerated(EnumType.ORDINAL)
    @Convert(converter = ProductRateConverter.class)
    private ProductRate rate;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
