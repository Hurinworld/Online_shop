package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "discounts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int discountPercent;

    //TODO check this annotation //done
//    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
