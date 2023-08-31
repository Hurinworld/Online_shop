package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "shop")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double income;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shop")
    private List<Product> productData;
}
