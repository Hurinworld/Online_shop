package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ShopRepository;
import com.serhiihurin.shop.online_shop.dto.ShopRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.enums.Role;
import com.serhiihurin.shop.online_shop.services.interfaces.ShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class ShopServiceImplTest {
    @Mock
    private ShopRepository shopRepository;
    private ShopService shopService;
    private ModelMapper modelMapper;
    private Shop testShop;
    private User testShopOwner;

    @BeforeEach
    void setUp() {
        shopService = new ShopServiceImpl(shopRepository);
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        testShopOwner = User.builder()
                .id(1L)
                .firstName("Shop owner")
                .lastName("For test")
                .email("testShopOwner@gmail.com")
                .password("password")
                .cash(39665.0)
                .role(Role.SHOP_OWNER)
                .build();

        testShop = Shop.builder()
                .id(1L)
                .name("Telemart")
                .income(1564626.0)
                .owner(testShopOwner)
                .build();
    }

    @Test
    void getAllShops() {
        //when
        shopService.getAllShops();
        //then
        Mockito.verify(shopRepository).findAll();
    }

    @Test
    void getShop() {
        //when
        Mockito.when(shopRepository.findById(testShop.getId())).thenReturn(Optional.of(testShop));
        Shop capturedValue = shopService.getShop(testShop.getId());
        //then
        assertThat(capturedValue).isEqualTo(testShop);
    }

    @Test
    void getShopByOwnerId() {
        //when
        Mockito.when(shopRepository.getShopByOwnerId(testShopOwner.getId())).thenReturn(Optional.of(testShop));
        Shop capturedValue = shopService.getShopByOwnerId(testShopOwner.getId());
        //then
        assertThat(capturedValue).isEqualTo(testShop);
    }

    @Test
    void updateShop() {
        //given
        String newName = "Megamart";
        Double newIncome = 3000000.0;
        ShopRequestDTO shopRequestDTO = new ShopRequestDTO();
        shopRequestDTO.setName(newName);
        shopRequestDTO.setIncome(newIncome);
        //when
        shopService.updateShop(shopRequestDTO, testShop);
        //then
        ArgumentCaptor<Shop> shopArgumentCaptor = ArgumentCaptor.forClass(Shop.class);
        Mockito.verify(shopRepository).save(shopArgumentCaptor.capture());
        Shop capturedValue = shopArgumentCaptor.getValue();
        assertThat(capturedValue.getName()).isEqualTo(newName);
        assertThat(capturedValue.getIncome()).isEqualTo(newIncome);
        assertThat(capturedValue.getOwner()).isEqualTo(testShopOwner);
    }

    @Test
    void createShop() {
        testShop.setId(null);
        ShopRequestDTO shopRequestDTO = modelMapper.map(testShop, ShopRequestDTO.class);
        //when
        shopService.createShop(testShopOwner, shopRequestDTO);
        //then
        ArgumentCaptor<Shop> shopArgumentCaptor = ArgumentCaptor.forClass(Shop.class);
        Mockito.verify(shopRepository).save(shopArgumentCaptor.capture());
        Shop capturedValue = shopArgumentCaptor.getValue();
        assertThat(capturedValue).isEqualTo(testShop);
    }

    @Test
    void deleteShop() {
        //when
        shopService.deleteShop(testShop.getId());
        //then
        Mockito.verify(shopRepository).deleteById(testShop.getId());
    }
}