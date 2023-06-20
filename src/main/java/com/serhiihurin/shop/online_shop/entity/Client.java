package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"shoppingCart", "purchasedProducts"})
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double cash;

    @ManyToMany
    @JoinTable(name = "clients_products",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<ProductData> shoppingCart;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private List<Purchase> purchasedProducts;

    public Client(String name, Double cash) {
        this.name = name;
        this.cash = cash;
    }
}
