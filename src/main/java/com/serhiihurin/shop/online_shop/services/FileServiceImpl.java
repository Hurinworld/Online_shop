package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.*;
import com.serhiihurin.shop.online_shop.entity.Image;
import com.serhiihurin.shop.online_shop.entity.ProductImage;
import com.serhiihurin.shop.online_shop.entity.UserImage;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.services.interfaces.FileService;
import com.serhiihurin.shop.online_shop.services.interfaces.ImageTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final ImageTokenService imageTokenService;
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;

    @Value("${custom.files-saving-path}")
    private String fileSavingPath;

    @Override
    public List<ProductImage> saveProductImages(Long productId, MultipartFile[] files) {
        List<ProductImage> productImages = new ArrayList<>();
        List<Image> images = saveImages(files);

        for (Image image : images) {
            productImages.add(
                    productImageRepository.save(
                            ProductImage.builder()
                                    .product(
                                            productRepository.findById(productId)
                                                    .orElseThrow(
                                                            () -> new ApiRequestException(
                                                                    "Could not find product with id: " + productId)
                                                    )
                                    )
                                    .imageInfo(image)
                                    .build()
                    )
            );
        }
        return productImages;
    }

    @Override
    public List<UserImage> saveUserImages(Long userId, MultipartFile[] files) {
        List<UserImage> userImages = new ArrayList<>();
        List<Image> images = saveImages(files);

        for (Image image : images) {
            userImages.add(
                    userImageRepository.save(
                            UserImage.builder()
                                    .user(
                                            userRepository.findById(userId)
                                                    .orElseThrow(
                                                            () -> new ApiRequestException(
                                                                    "Could not find user with id: " + userId)
                                                    )
                                    )
                                    .imageInfo(image)
                                    .build()
                    )
            );
        }
        return userImages;
    }


    @Override
    public byte[] getImage(String filepath) {
        try {
            return Files.readAllBytes(Path.of(filepath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Image> saveImages(MultipartFile[] files) {
        List<Image> images = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String filePath = fileSavingPath + file.getOriginalFilename();
                    File destFile = new File(filePath);
                    file.transferTo(destFile);
                    images.add(
                            imageRepository.save(
                                    Image.builder()
                                            .filepath(filePath)
                                            .imageToken(imageTokenService.createImageToken(filePath))
                                            .build()
                            )
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return images;
    }
}
