package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "purchases")
@NoArgsConstructor
@Data
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime time;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchase")
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Purchase(LocalDateTime time) {
        this.time = time;
    }
}
