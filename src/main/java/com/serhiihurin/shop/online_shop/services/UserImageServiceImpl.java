package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.UserImageRepository;
import com.serhiihurin.shop.online_shop.entity.Image;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.UserImage;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.services.interfaces.UserImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserImageServiceImpl implements UserImageService {
    private final UserImageRepository userImageRepository;

    @Override
    public List<UserImage> saveUserImages(User user, List<Image> images) {
        List<UserImage> userImages = new ArrayList<>();
        for (Image image : images) {
            userImages.add(
                    userImageRepository.save(
                            UserImage.builder()
                                    .user(user)
                                    .imageInfo(image)
                                    .build()
                    )
            );
        }
        return userImages;
    }

    @Override
    public UserImage getUserImageByImageToken(String imageToken) {
        return userImageRepository.getUserImageByImageToken(imageToken)
                .orElseThrow(() -> new ApiRequestException("Could not find image by specified token"));
    }

    @Override
    public List<UserImage> getUserImagesByUserId(Long userId) {
        return userImageRepository.getUserImagesByUserId(userId);
    }

    @Override
    public void deleteUserImage(UserImage userImage) {
        userImageRepository.delete(userImage);
    }
}
