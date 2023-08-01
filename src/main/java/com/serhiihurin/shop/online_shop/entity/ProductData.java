package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "products_data")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString(exclude = {"feedbacks","users"})
public class ProductData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private int count;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productData")
    private List<Feedback> feedbacks;

    @ManyToMany()
    @JoinTable(name = "clients_products",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private List<User> users;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productData")
    private List<Product> products;

    public ProductData(String name, String description, Double price, int count) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.count = count;
    }
}
