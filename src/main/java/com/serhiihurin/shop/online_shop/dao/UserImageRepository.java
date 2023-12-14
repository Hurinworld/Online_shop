package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.ProductImage;
import com.serhiihurin.shop.online_shop.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    List<UserImage> getUserImagesByUserId(Long userId);

    @Query("SELECT userImage FROM UserImage userImage JOIN userImage.imageInfo " +
            "WHERE userImage.imageInfo.imageToken = :imageToken")
    UserImage getUserImageByImageToken(@Param("imageToken") String imageToken);
}
