package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filepath;
    //TODO change naming to more specifying //done
    private String imageToken;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
