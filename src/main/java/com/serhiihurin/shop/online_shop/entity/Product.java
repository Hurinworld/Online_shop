package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serialNumber;
    private Double weight;

    @ManyToOne
    @JoinColumn(name = "product_data_id")
    private ProductData productData;

    public Product(String serialNumber, Double weight) {
        this.serialNumber = serialNumber;
        this.weight = weight;
    }
}
