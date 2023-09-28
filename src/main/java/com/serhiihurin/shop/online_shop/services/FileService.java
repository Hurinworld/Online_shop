package com.serhiihurin.shop.online_shop.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<byte[]> saveProductImages(Long productId, MultipartFile[] files);
}
