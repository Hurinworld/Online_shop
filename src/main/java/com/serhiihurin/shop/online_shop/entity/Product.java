package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private int amount;
    private boolean onSale;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<Feedback> feedbacks;

    @ToString.Exclude
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "product")
    private List<ProductImage> productImages;

    @ManyToMany(mappedBy = "shoppingCart")
    private List<User> users;

    @ManyToMany(mappedBy = "productAvailabilityList")
    private List<User> ProductAvailabilitySubscriptionList;
}
