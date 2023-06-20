package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"feedbacks","clients"})
public class ProductData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private int price;
    private int count;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<Feedback> feedbacks;

    @ManyToMany()
    @JoinTable(name = "clients_products",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private List<Client> clients;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productData")
    private List<Product> products;

    public ProductData(String name, String description, int price, int count) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.count = count;
    }
}
