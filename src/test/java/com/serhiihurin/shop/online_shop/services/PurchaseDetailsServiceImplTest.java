package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.PurchaseDetailsRepository;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.Purchase;
import com.serhiihurin.shop.online_shop.entity.PurchaseDetails;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.enums.Role;
import com.serhiihurin.shop.online_shop.services.interfaces.PurchaseDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class PurchaseDetailsServiceImplTest {
    @Mock
    private PurchaseDetailsRepository purchaseDetailsRepository;
    private PurchaseDetailsService purchaseDetailsService;
    private PurchaseDetails testPurchaseDetails;
    private User testUser;

    @BeforeEach
    void setUp() {
        purchaseDetailsService = new PurchaseDetailsServiceImpl(purchaseDetailsRepository);
        testUser = User.builder()
                .id(1L)
                .firstName("User")
                .lastName("For test")
                .email("testUser@gmail.com")
                .password("password")
                .cash(26665.0)
                .role(Role.CLIENT)
                .build();
        Product testProduct = Product.builder()
                .id(1L)
                .name("AMD Ryzen 7 5700X")
                .description("CPU")
                .amount(100)
                .price(8500.0)
                .build();
        Purchase testPurchase = new Purchase();
        testPurchase.setId(1L);
        testPurchase.setTime(LocalDateTime.now());
        testPurchase.setUser(testUser);
        testPurchaseDetails = PurchaseDetails.builder()
                .id(1L)
                .amount(3)
                .purchase(testPurchase)
                .product(testProduct)
                .totalPrice(testProduct.getPrice() * 3)
                .build();
    }

    @Test
    void savePurchaseDetails() {
        //when
        purchaseDetailsService.savePurchaseDetails(testPurchaseDetails);
        //then
        ArgumentCaptor<PurchaseDetails> purchaseDetailsArgumentCaptor = ArgumentCaptor.forClass(PurchaseDetails.class);
        Mockito.verify(purchaseDetailsRepository).save(purchaseDetailsArgumentCaptor.capture());
        PurchaseDetails capturedValue = purchaseDetailsArgumentCaptor.getValue();
        assertThat(capturedValue).isEqualTo(testPurchaseDetails);
    }
}