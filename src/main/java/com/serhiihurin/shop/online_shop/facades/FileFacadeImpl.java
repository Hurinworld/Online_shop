package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.ProductImage;
import com.serhiihurin.shop.online_shop.facades.interfaces.FileFacade;
import com.serhiihurin.shop.online_shop.services.interfaces.FileService;
import com.serhiihurin.shop.online_shop.services.interfaces.ImageTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileFacadeImpl implements FileFacade {
    private final FileService fileService;
    private final ImageTokenService imageTokenService;

    @Value("${custom.image-retrieve-endpoint}")
    private String imageRetrieveEndpoint;
    @Override
    public List<String> saveProductImages(Long productId, MultipartFile[] files) {
        List<String> imageEndpoints = new ArrayList<>();
        List<ProductImage> productImages = fileService.saveProductImages(productId, files);
        for (ProductImage productImage : productImages) {
            imageEndpoints.add(
                    imageRetrieveEndpoint +
                    productImage.getImageToken()
            );
        }
        return imageEndpoints;
    }

    @Override
    public byte[] getProductImage(String imageToken) {
        return fileService.getProductImage(imageTokenService.getPathByImageToken(imageToken));
    }
}
