package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int cash;

    @ManyToMany
    @JoinTable(name = "client_products",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> shoppingCart;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private List<Purchase> purchasedProducts;

    public Client() {
    }

    public Client(String name, int cash) {
        this.name = name;
        this.cash = cash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public List<Product> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(List<Product> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public List<Purchase> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(List<Purchase> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cash=" + cash +
                '}';
    }
}
