package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.ProductImage;
import com.serhiihurin.shop.online_shop.entity.UserImage;
import com.serhiihurin.shop.online_shop.exception.UnauthorizedAccessException;
import com.serhiihurin.shop.online_shop.facades.interfaces.FileFacade;
import com.serhiihurin.shop.online_shop.services.interfaces.*;
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
    private final ProductService productService;
    private final UserImageService userImageService;
    private final ProductImageService productImageService;
    private final ImageTokenService imageTokenService;

    @Value("${custom.image-retrieve-endpoint}")
    private String imageRetrieveEndpoint;
    @Override
    public List<String> saveProductImages(Long userId, Long productId, MultipartFile[] files) {
        if (!productService.getProduct(productId).getShop().getOwner().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Access denied. You don't have authorities to modify this product.");
        }
        List<String> imageEndpoints = new ArrayList<>();
        List<ProductImage> productImages = fileService.saveProductImages(productId, files);
        for (ProductImage productImage : productImages) {
            imageEndpoints.add(
                    imageRetrieveEndpoint +
                    productImage.getImageInfo().getImageToken()
            );
        }
        return imageEndpoints;
    }

    @Override
    public List<String> saveUserImages(Long userId, MultipartFile[] files) {
        List<String> imageEndpoints = new ArrayList<>();
        List<UserImage> userImages = fileService.saveUserImages(userId, files);
        for (UserImage userImage : userImages) {
            imageEndpoints.add(
                    imageRetrieveEndpoint +
                    userImage.getImageInfo().getImageToken()
            );
        }
        return imageEndpoints;
    }

    @Override
    public void deleteUserImage(String imageToken, Long userId) {
        UserImage userImage = userImageService.getUserImageByImageToken(imageToken);
        if (!userImage.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Access denied. Wrong image token");
        }
        fileService.deleteImage(imageTokenService.getPathByImageToken(imageToken));
        userImageService.deleteUserImage(userImage);
    }

    @Override
    public void deleteProductImage(String imageToken, Long userId) {
        ProductImage productImage = productImageService.getProductImageByImageToken(imageToken);
        if (!productImage.getProduct().getShop().getOwner().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Access denied. Wrong image token");
        }
        fileService.deleteImage(imageTokenService.getPathByImageToken(imageToken));
        productImageService.deleteProductImage(productImage);
    }

    @Override
    public byte[] getImage(String imageToken) {
        return fileService.getImage(imageTokenService.getPathByImageToken(imageToken));
    }
}
