package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.WishlistRepository;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.Wishlist;
import com.serhiihurin.shop.online_shop.entity.composite_id.UserProductId;
import com.serhiihurin.shop.online_shop.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class WishlistServiceImplTest {
    @Mock
    private WishlistRepository wishlistRepository;
    private WishlistService wishlistService;
    private Wishlist testWishList;
    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        wishlistService = new WishlistServiceImpl(wishlistRepository);
        testUser = User.builder()
                .id(1L)
                .firstName("User")
                .lastName("For test")
                .email("testUser@gmail.com")
                .password("password")
                .cash(26665.0)
                .role(Role.CLIENT)
                .build();
        testProduct = Product.builder()
                .id(1L)
                .name("AMD Ryzen 7 5700X")
                .description("CPU")
                .amount(100)
                .price(8500.0)
                .build();
        testWishList = Wishlist.builder()
                .id(
                        UserProductId.builder()
                        .userId(testUser.getId())
                        .productId(testProduct.getId())
                        .build()
                )
                .user(testUser)
                .product(testProduct)
                .build();
    }

    @Test
    void getWishlistsByProductsOnSale() {
        //given
        testProduct.setOnSale(true);
        List<Wishlist> expectedWishlists = List.of(testWishList);

        Mockito.when(wishlistRepository.getWishlistsByProductOnSale(true)).thenReturn(expectedWishlists);
        // when
        List<Wishlist> actualWishlists = wishlistService.getWishlistsByProductsOnSale();
        // then
        assertThat(expectedWishlists).isEqualTo(actualWishlists);
        Mockito.verify(wishlistRepository).getWishlistsByProductOnSale(true);
    }

    @Test
    void getUserWishlist() {
        //given
        List<Wishlist> expectedWishlists = List.of(testWishList);
        Mockito.when(wishlistRepository.findAllByUserId(testUser.getId())).thenReturn(expectedWishlists);
        //when
        List<Wishlist> actualWishlists = wishlistService.getUserWishlist(testUser.getId());
        //then
        assertThat(expectedWishlists).isEqualTo(actualWishlists);
        Mockito.verify(wishlistRepository).findAllByUserId(testUser.getId());
    }

    @Test
    void addProductToWishlist() {
        //when
        wishlistService.addProductToWishlist(testUser, testProduct);
        //then
        ArgumentCaptor<Wishlist> wishlistArgumentCaptor = ArgumentCaptor.forClass(Wishlist.class);
        Mockito.verify(wishlistRepository).save(wishlistArgumentCaptor.capture());
        Wishlist capturedValue = wishlistArgumentCaptor.getValue();
        assertThat(capturedValue).usingRecursiveComparison().isEqualTo(testWishList);
    }

    @Test
    void deleteProductFromWishlist() {
        //when
        wishlistService.deleteProductFromWishlist(testUser.getId(), testProduct.getId());
        //then
        Mockito.verify(wishlistRepository).deleteByProductIdAndUserId(testUser.getId(), testProduct.getId());
    }
}