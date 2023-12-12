package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.PurchaseRepository;
import com.serhiihurin.shop.online_shop.entity.*;
import com.serhiihurin.shop.online_shop.enums.Role;
import com.serhiihurin.shop.online_shop.services.interfaces.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceImplTest {
    @Mock
    private PurchaseRepository purchaseRepository;
    private PurchaseService purchaseService;
    private Purchase testPurchase;
    private User testUser;

    @BeforeEach
    void setUp() {
        purchaseService = new PurchaseServiceImpl(purchaseRepository);
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
                .amount(100)
                .price(8500.0)
                .description("CPU")
                .build();
        PurchaseDetails testPurchaseDetails = PurchaseDetails.builder()
                .id(1L)
                .product(testProduct)
                .amount(3)
                .totalPrice(testProduct.getPrice() * 3.0)
                .build();
        testPurchase = new Purchase();
        testPurchase.getPurchaseDetails().add(testPurchaseDetails);
        testPurchase.setUser(testUser);
        testPurchase.setTime(LocalDateTime.now());

    }

    @Test
    void getAllPurchases() {
        //when
        purchaseService.getAllPurchases();
        //then
        Mockito.verify(purchaseRepository).findAll();
    }

    @Test
    void getPurchasesByClientId() {
        // given
        List<Purchase> expectedPurchases = List.of(testPurchase);

        Mockito.when(purchaseRepository.getPurchasesByUserId(testUser.getId())).thenReturn(expectedPurchases);
        // when
        List<Purchase> actualPurchases = purchaseService.getPurchasesByClientId(testUser.getId());
        // then
        assertThat(expectedPurchases).isEqualTo(actualPurchases);
        Mockito.verify(purchaseRepository).getPurchasesByUserId(testUser.getId());
    }

    @Test
    void savePurchase() {
        //when
        purchaseService.savePurchase(testPurchase);
        //then
        ArgumentCaptor<Purchase> purchaseArgumentCaptor = ArgumentCaptor.forClass(Purchase.class);
        Mockito.verify(purchaseRepository).save(purchaseArgumentCaptor.capture());
        Purchase capturedValue = purchaseArgumentCaptor.getValue();
        assertThat(capturedValue).isEqualTo(testPurchase);
    }

    @Test
    void getPurchase() {
        //when
        Mockito.when(purchaseRepository.findById(testPurchase.getId())).thenReturn(Optional.of(testPurchase));
        Purchase capturedValue = purchaseService.getPurchase(testPurchase.getId());
        //then
        assertThat(capturedValue).isEqualTo(testPurchase);
    }

    @Test
    void deletePurchase() {
        //when
        purchaseService.deletePurchase(testPurchase.getId());
        //then
        Mockito.verify(purchaseRepository).deleteById(testPurchase.getId());
    }
}