package com.serhiihurin.shop.online_shop.services.interfaces;

import com.serhiihurin.shop.online_shop.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<Image> saveImages(MultipartFile[] files);

    byte[] getImage(String filepath);

    void deleteImage(String filepath);
}
