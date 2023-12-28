package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ProductImageRepository;
import com.serhiihurin.shop.online_shop.entity.Image;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.ProductImage;
import com.serhiihurin.shop.online_shop.services.interfaces.ProductImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductImageServiceImplTest {
    @Mock
    private ProductImageRepository productImageRepository;
    private ProductImageService productImageService;
    private Image testImage;
    private ProductImage testProductImage;
    private Product testProduct;


    @BeforeEach
    void setUp() {
        productImageService = new ProductImageServiceImpl(productImageRepository);
        testProduct = Product.builder()
                .id(1L)
                .name("AMD Ryzen 7 5700X")
                .description("CPU")
                .amount(100)
                .price(8500.0)
                .build();
        testImage = Image.builder()
                .filepath("test file path")
                .imageToken("test image token")
                .build();
        testProductImage = ProductImage.builder()
                .id(1L)
                .product(testProduct)
                .imageInfo(testImage)
                .build();
    }

    @Test
    void getProductImagesByProductId() {
        // given
        List<ProductImage> expectedProductImages = List.of(testProductImage);

        Mockito.when(productImageRepository.getProductImagesByProductId(testProduct.getId()))
                .thenReturn(expectedProductImages);
        // when
        List<ProductImage> actualProductImages = productImageService.getProductImagesByProductId(testProduct.getId());
        // then
        assertThat(expectedProductImages).isEqualTo(actualProductImages);
        Mockito.verify(productImageRepository).getProductImagesByProductId(testProduct.getId());
    }

    @Test
    void addProductImage() {
        //when
        productImageService.addProductImage(testProduct, new ArrayList<>(List.of(testImage)));
        //then
        ArgumentCaptor<ProductImage> productImageArgumentCaptor = ArgumentCaptor.forClass(ProductImage.class);
        Mockito.verify(productImageRepository).save(productImageArgumentCaptor.capture());
        ProductImage capturedValue = productImageArgumentCaptor.getValue();
        assertThat(capturedValue).isEqualTo(testProductImage);
    }
}