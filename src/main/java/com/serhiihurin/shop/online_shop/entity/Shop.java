package com.serhiihurin.shop.online_shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "shop")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"productData"})
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shop")
    @JsonIgnore
    private List<ProductData> productData;

    private Double income;

    public Shop(String name, Double income) {
        this.name = name;
        this.income = income;
    }
}
