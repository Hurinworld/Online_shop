package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "purchases")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime time;

    @OneToMany
    @JoinColumn(name = "purchase_id")
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Purchase(LocalDateTime time) {
        this.time = time;
    }
}
