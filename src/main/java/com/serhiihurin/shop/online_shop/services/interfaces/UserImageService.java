package com.serhiihurin.shop.online_shop.services.interfaces;

import com.serhiihurin.shop.online_shop.entity.Image;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.UserImage;

import java.util.List;

public interface UserImageService {
    List<UserImage> saveUserImages(User user, List<Image> images);

    UserImage getUserImageByImageToken(String imageToken);

    List<UserImage> getUserImagesByUserId(Long userId);

    void deleteUserImage(UserImage userImage);
}
