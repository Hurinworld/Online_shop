package com.serhiihurin.shop.online_shop.entity;

import com.serhiihurin.shop.online_shop.entity.composite_id.UserProductId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wishlists")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Wishlist {
    @EmbeddedId
    private UserProductId id;

    @ManyToOne
    @MapsId(value = "userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId(value = "productId")
    @JoinColumn(name = "product_id")
    private Product product;
}
