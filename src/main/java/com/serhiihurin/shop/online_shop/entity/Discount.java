package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "discounts")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Discount {
    @Id
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int discountPercent;
}
