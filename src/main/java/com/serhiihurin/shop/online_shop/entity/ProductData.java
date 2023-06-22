package com.serhiihurin.shop.online_shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "products_data")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"feedbacks","clients"})
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
    @JsonIgnore
    private Shop shop;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productData")
    @JsonIgnore
    private List<Feedback> feedbacks;

    @ManyToMany()
    @JoinTable(name = "clients_products",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    @JsonIgnore
    private List<Client> clients;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productData")
    @JsonIgnore
    private List<Product> products;

    public ProductData(String name, String description, Double price, int count) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.count = count;
    }
}
