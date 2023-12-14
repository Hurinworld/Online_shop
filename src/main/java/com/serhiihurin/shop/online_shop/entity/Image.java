package com.serhiihurin.shop.online_shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image_info")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Image {
    @Id
    private String imageToken;
    private String filepath;

    @OneToOne(mappedBy = "imageInfo")
    private UserImage userImage;

    @OneToOne(mappedBy = "imageInfo")
    private ProductImage productImage;
}
