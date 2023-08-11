package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isBought;
    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;
    @ManyToOne
    @JoinColumn(name = "product_data_id")
    private ProductData productData;
}
