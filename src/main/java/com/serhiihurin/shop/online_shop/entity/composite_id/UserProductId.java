package com.serhiihurin.shop.online_shop.entity.composite_id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProductId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_id")
    private Long productId;
}
