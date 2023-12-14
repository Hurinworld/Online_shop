package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ImageRepository;
import com.serhiihurin.shop.online_shop.entity.Image;
import com.serhiihurin.shop.online_shop.services.interfaces.ImageTokenService;
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
    private ImageRepository imageRepository;
    private ImageTokenService imageTokenService;

    @BeforeEach
    void setUp() {
        imageTokenService = new ImageTokenServiceImpl(imageRepository);
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
        Image image = new Image();
        image.setFilepath(expectedPath);

        Mockito.when(imageRepository.getImageByImageToken(imageToken)).thenReturn(image);

        //when
        String imagePath = imageTokenService.getPathByImageToken(imageToken);

        //then
        assertEquals(expectedPath, imagePath);
        Mockito.verify(imageRepository).getImageByImageToken(imageToken);
    }
}