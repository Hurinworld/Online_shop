package com.serhiihurin.shop.online_shop.entity;

import com.serhiihurin.shop.online_shop.enums.ProductRate;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private LocalDateTime time;
    @Enumerated(EnumType.STRING)
    private ProductRate rate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductData productData;

    public Feedback(String text, ProductRate rate) {
        this.text = text;
        this.rate = rate;
    }
}
