package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString(exclude = {"feedbacks", "users"})
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

    @ManyToMany(mappedBy = "shoppingCart")
    private List<User> users;

    public Product(String name, String description, Double price, int amount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }
}
