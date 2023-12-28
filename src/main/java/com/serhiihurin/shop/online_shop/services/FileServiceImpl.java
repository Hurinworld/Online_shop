package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.*;
import com.serhiihurin.shop.online_shop.entity.Image;
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

    @Value("${custom.files-saving-path}")
    private String fileSavingPath;

    @Override
    public List<Image> saveImages(MultipartFile[] files) {
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

    @Override
    public byte[] getImage(String filepath) {
        try {
            return Files.readAllBytes(Path.of(filepath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteImage(String filepath) {
        try {
            Files.deleteIfExists(Path.of(filepath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
