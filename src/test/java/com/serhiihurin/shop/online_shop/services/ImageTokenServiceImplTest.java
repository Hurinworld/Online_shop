package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ProductImageRepository;
import com.serhiihurin.shop.online_shop.entity.ProductImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageTokenServiceImplTest {
    @Mock
    private ProductImageRepository productImageRepository;
    private ImageTokenService imageTokenService;

    @BeforeEach
    void setUp() {
        imageTokenService = new ImageTokenServiceImpl(productImageRepository);
    }

    @Test
    void createImageToken() {
        //given
        String filepath = "path/to/your/image.jpg";
        //when
        String imageToken = imageTokenService.createImageToken(filepath);
        //then
        assertNotNull(imageToken);
        assertNotEquals("", imageToken);
    }

    @Test
    void getPathByImageToken() {
        //given
        String imageToken = "valid-image-token";
        String expectedPath = "path/to/your/image.jpg";
        ProductImage productImage = new ProductImage();
        productImage.setFilepath(expectedPath);

        Mockito.when(productImageRepository.getProductImageByImageToken(imageToken)).thenReturn(productImage);

        //when
        String imagePath = imageTokenService.getPathByImageToken(imageToken);

        //then
        assertEquals(expectedPath, imagePath);
        Mockito.verify(productImageRepository).getProductImageByImageToken(imageToken);
    }
}