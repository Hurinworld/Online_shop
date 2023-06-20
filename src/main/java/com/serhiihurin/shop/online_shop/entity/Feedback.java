package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private int rate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductData productData;

    public Feedback(String text, LocalDateTime time, int rate) {
        this.text = text;
        this.time = time;
        this.rate = rate;
    }
}
