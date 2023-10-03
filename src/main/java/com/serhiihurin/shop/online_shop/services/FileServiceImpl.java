package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ProductImageRepository;
import com.serhiihurin.shop.online_shop.dao.ProductRepository;
import com.serhiihurin.shop.online_shop.entity.ProductImage;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
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
public class FileServiceImpl implements FileService{
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    @Value("${custom.files-saving-path}")
    private String fileSavingPath;

    @Override
    public List<byte[]> saveProductImages(Long productId, MultipartFile[] files) {
        List<byte[]> productImages = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String filePath = fileSavingPath + file.getOriginalFilename();
                    File destFile = new File(filePath);
                    file.transferTo(destFile);
                    productImages.add(Files.readAllBytes(Path.of(filePath)));
                    productImageRepository.save(
                            ProductImage.builder()
                                    .filepath(filePath)
                                    .product(
                                            productRepository.findById(productId)
                                                    .orElseThrow(() -> new ApiRequestException(
                                                            "Could not find product with ID: " + productId)
                                                    )
                                    )
                                    .build()
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return productImages;
    }
}