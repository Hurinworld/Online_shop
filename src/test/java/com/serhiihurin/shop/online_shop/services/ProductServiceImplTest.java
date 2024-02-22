package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.*;
import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.entity.*;
import com.serhiihurin.shop.online_shop.enums.Role;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.exception.UnauthorizedAccessException;
import com.serhiihurin.shop.online_shop.services.interfaces.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductImageRepository productImageRepository;
    @Mock
    private ShopRepository shopRepository;
    @Mock
    private DiscountRepository discountRepository;
    @Mock
    private EventRepository eventRepository;
    private ProductService productService;
    private ModelMapper modelMapper;
    private Product testProduct;
    private Event testEvent;
    private Discount testDiscount;
    private User testUser;
    private Shop testShop;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(
                productRepository,
                productImageRepository,
                shopRepository,
                discountRepository,
                eventRepository
        );
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        testUser = User.builder()
                .id(1L)
                .firstName("User")
                .lastName("For test")
                .email("testUser@gmail.com")
                .password("password")
                .cash(26665.0)
                .role(Role.SHOP_OWNER)
                .build();
        testShop = Shop.builder()
                .id(1L)
                .name("Test shop")
                .income(325665.0)
                .owner(testUser)
                .build();
        testProduct = Product.builder()
                .id(1L)
                .name("AMD Ryzen 7 5700X")
                .description("CPU")
                .amount(100)
                .price(8500.0)
                .shop(testShop)
                .build();
        testEvent = Event.builder()
                .id(1L)
                .title("Test event")
                .description("Test event description")
                .eventCreator(testUser)
                .startDateTime(LocalDateTime.parse(
                        LocalDateTime.now().format(formatter), formatter
                    )
                )
                .endDateTime(
                        LocalDateTime.parse(
                        LocalDateTime.now().format(formatter), formatter
                    ).plusHours(1)
                )
                .build();
        testDiscount = Discount.builder()
                .id(1L)
                .discountPercent(15)
                .product(testProduct)
                .event(testEvent)
                .build();
    }

    @Test
    void getAllProducts() {
        //when
        productService.getAllProducts();
        //then
        Mockito.verify(productRepository).findAll();
    }

    @Test
    void getProductsByShopId() {
        // given
        testShop.setProducts(List.of(testProduct));
        List<Product> expectedProducts = testShop.getProducts();

        Mockito.when(productRepository.getProductsByShopId(testShop.getId())).thenReturn(expectedProducts);
        // when
        List<Product> actualPurchases = productService.getProductsByShopId(testShop.getId());
        // then
        assertThat(expectedProducts).isEqualTo(actualPurchases);
        Mockito.verify(productRepository).getProductsByShopId(testShop.getId());
    }

    @Test
    void getProduct() {
        //when
        Mockito.when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));
        Product capturedValue = productService.getProduct(testProduct.getId());
        //then
        assertThat(capturedValue).isEqualTo(testProduct);
    }

    @Test
    void addProduct() {
        testProduct.setId(null);
        ProductRequestDTO productRequestDTO = modelMapper.map(testProduct, ProductRequestDTO.class);
        Mockito.when(shopRepository.findById(testShop.getId())).thenReturn(Optional.of(testShop));
        //when
        productService.addProduct(productRequestDTO);
        //then
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        Mockito.verify(productRepository).save(productArgumentCaptor.capture());
        Product capturedValue = productArgumentCaptor.getValue();
        assertThat(capturedValue).usingRecursiveComparison().isEqualTo(testProduct);
    }

    @Test
    void addProductWithWrongShopId() {
        //given
        ProductRequestDTO productRequestDTO = modelMapper.map(testProduct, ProductRequestDTO.class);
        productRequestDTO.setShopId(null);
        // when
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> productService.addProduct(productRequestDTO));

        // then
        assertThat(exception.getMessage())
                .isEqualTo("Could not find shop with ID: " + productRequestDTO.getShopId());
        Mockito.verifyNoInteractions(productRepository);
    }

    @Test
    void updateProduct() {
        //given
        String newName = "AMD Ryzen 7 5700X test";
        String newDescription = "CPU test";
        Double newPrice = 8010.0;
        ProductRequestDTO productRequestDTO = modelMapper.map(testProduct, ProductRequestDTO.class);
        productRequestDTO.setName(newName);
        productRequestDTO.setDescription(newDescription);
        productRequestDTO.setPrice(newPrice);
        //when
        productService.updateProduct(testUser, productRequestDTO, testProduct);
        //then
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        Mockito.verify(productRepository).save(productArgumentCaptor.capture());
        Product capturedValue = productArgumentCaptor.getValue();
        assertThat(capturedValue.getName()).isEqualTo(newName);
        assertThat(capturedValue.getDescription()).isEqualTo(newDescription);
        assertThat(capturedValue.getPrice()).isEqualTo(newPrice);
        assertThat(capturedValue.getAmount()).isEqualTo(testProduct.getAmount());
    }

    @Test
    void updateProduct_WithWrong_OwnerID() {
        //given
        User wrongOwner = User.builder()
                .id(10L)
                .build();
        ProductRequestDTO productRequestDTO = modelMapper.map(testProduct, ProductRequestDTO.class);
        //when
        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class,
                () -> productService.updateProduct(wrongOwner, productRequestDTO, testProduct));
        //then
        assertThat(exception.getMessage()).isEqualTo("Access denied. Wrong product ID");
        Mockito.verifyNoInteractions(productRepository);
    }

    @Test
    void putProductOnSale() {
        //given
        testProduct.setPrice(100.0);

        Mockito.when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));
        //when
        productService.putProductOnSale(testUser, testProduct.getId(), testDiscount.getDiscountPercent(), null);
        //then
        assertTrue(testProduct.isOnSale());
        assertEquals(
                85,
                testProduct.getPrice()
        );

        Mockito.verify(discountRepository).save(ArgumentMatchers.any(Discount.class));
        Mockito.verify(productRepository).save(testProduct);
    }

    @Test
    void putProductOnSale_WithWrong_OwnerID() {
        //given
        User wrongOwner = User.builder()
                .id(10L)
                .role(Role.SHOP_OWNER)
                .build();
        Mockito.when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));
        //when
        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class,
                () -> productService.putProductOnSale(
                        wrongOwner, testProduct.getId(), testDiscount.getDiscountPercent(), null
                ));
        //then
        assertThat(exception.getMessage()).isEqualTo("Access denied. Wrong product ID");
    }

    @Test
    void putProductOnSale_WithWrong_DiscountPercent() {
        //given
        testDiscount.setDiscountPercent(-3);
        Mockito.when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));
        //when
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> productService.putProductOnSale(
                        testUser, testProduct.getId(), testDiscount.getDiscountPercent(), null
                ));
        //then
        assertThat(exception.getMessage()).isEqualTo("Invalid discount percent. " +
                "The value should be higher than 1 and lower than 100");
        //given
        testDiscount.setDiscountPercent(113);
        //when
        exception = assertThrows(ApiRequestException.class,
                () -> productService.putProductOnSale(
                        testUser, testProduct.getId(), testDiscount.getDiscountPercent(), null
                ));
        //then
        assertThat(exception.getMessage()).isEqualTo("Invalid discount percent. " +
                "The value should be higher than 1 and lower than 100");
    }

    @Test
    void putProductOnSale_ThatAlready_OnSale_ThrowsException() {
        //given
        testProduct.setOnSale(true);
        Mockito.when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));
        //when
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> productService.putProductOnSale(
                        testUser, testProduct.getId(), testDiscount.getDiscountPercent(), null
                ));
        //then
        assertThat(exception.getMessage()).isEqualTo("The product is already on sale");
    }

    @Test
    void getProductByProductSearchValue_WithValidId_ShouldReturnProduct()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        Method method =
                ProductServiceImpl.class.getDeclaredMethod("getProductByProductSearchValue", Object.class);
        method.setAccessible(true);
        Mockito.when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));
        //when
        Product actualProduct = (Product) method.invoke(productService, testProduct.getId());

        //then
        assertNotNull(actualProduct);
        assertEquals(testProduct.getId(), actualProduct.getId());
    }

    @Test
    void getProductByProductSearchValue_WithValidName_ShouldReturnProduct()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        Method method =
                ProductServiceImpl.class.getDeclaredMethod("getProductByProductSearchValue", Object.class);
        method.setAccessible(true);
        Mockito.when(productRepository.getProductByName(testProduct.getName())).thenReturn(Optional.of(testProduct));
        // Act
        Product actualProduct = (Product) method.invoke(productService, testProduct.getName());

        // Assert
        assertNotNull(actualProduct);
        assertEquals(testProduct.getName(), actualProduct.getName());
    }

    @Test
    void getProductByProductSearchValue_WithInvalidType_ShouldThrowException()
            throws NoSuchMethodException, IllegalAccessException {
        //given
        Double invalidProductSearchValue = 123.0; // Not Long or String

        Method method =
                ProductServiceImpl.class.getDeclaredMethod("getProductByProductSearchValue", Object.class);
        method.setAccessible(true);
        // then
        try {
            method.invoke(productService, invalidProductSearchValue);
            fail("Expected exception was not thrown");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof ApiRequestException);
            assertEquals("Wrong parameter value " + invalidProductSearchValue +
                    ". The value should be of type Long or String", cause.getMessage());
        }
        Mockito.verifyNoInteractions(productRepository);
    }

    @Test
    void getProductByProductSearchValue_WithNonExistingId_ShouldThrowException()
            throws NoSuchMethodException, IllegalAccessException {
        //given
        testProduct.setId(999L);

        Method method =
                ProductServiceImpl.class.getDeclaredMethod("getProductByProductSearchValue", Object.class);
        method.setAccessible(true);
        //then
        try {
            method.invoke(productService, testProduct.getId());
            fail("Expected exception was not thrown");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof ApiRequestException);
            assertEquals("Could not find product with ID: " + testProduct.getId(), cause.getMessage());
        }
    }

    @Test
    void getProductByProductSearchValue_WithNonExistingName_ShouldThrowException()
            throws NoSuchMethodException, IllegalAccessException {
        //given
        testProduct.setName("Nema takoho name");

        Method method =
                ProductServiceImpl.class.getDeclaredMethod("getProductByProductSearchValue", Object.class);
        method.setAccessible(true);
        //then
        try {
            method.invoke(productService, testProduct.getName());
            fail("Expected exception was not thrown");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof ApiRequestException);
            assertEquals("Could not find product with name: " + testProduct.getName(), cause.getMessage());
        }
    }

    @Test
    void removeProductFromSale() {
        //given
        testProduct.setPrice(85.0);

        Mockito.when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));
        Mockito.when(discountRepository.findByProductId(testProduct.getId())).thenReturn(Optional.of(testDiscount));
        //when
        productService.removeProductFromSale(testUser, testProduct.getId());
        //then
        assertFalse(testProduct.isOnSale());
        assertEquals(
                100.0,
                testProduct.getPrice()
        );

        Mockito.verify(discountRepository).deleteById(testDiscount.getId());
        Mockito.verify(productRepository).save(testProduct);
    }

    @Test
    void removeProductFromSale_WithWrong_OwnerID() {
        //given
        User wrongOwner = User.builder()
                .id(10L)
                .role(Role.SHOP_OWNER)
                .build();
        Mockito.when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));
        //when
        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class,
                () -> productService.removeProductFromSale(wrongOwner, testProduct.getId()));
        //then
        assertThat(exception.getMessage()).isEqualTo("Access denied. Wrong product ID");
    }

    @Test
    void removeEventProductsFromSale() {
        // given

        Mockito.when(eventRepository.findById(testEvent.getId())).thenReturn(Optional.of(testEvent));
        Mockito.when(discountRepository.findDiscountsByEventId(testEvent.getId()))
                .thenReturn(Collections.singletonList(testDiscount));
        Mockito.when(productRepository.save(testProduct)).thenReturn(testProduct);

        // when
        productService.removeEventProductsFromSale(testEvent.getId());

        // then
        Mockito.verify(productRepository).save(testProduct);
        Mockito.verify(discountRepository).deleteAll(Collections.singletonList(testDiscount));
    }

    @Test
    void deleteProduct() {
        //given
        Mockito.when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));
        Mockito.when(productImageRepository
                .getProductImagesByProductId(testProduct.getId())).thenReturn(new ArrayList<>());
        //when
        productService.deleteProduct(testUser, testProduct.getId());
        //then
        Mockito.verify(productRepository).deleteById(testProduct.getId());
    }

    @Test
    void deleteProduct_WithWrong_OwnerID() {
        //given
        User wrongOwner = User.builder()
                .id(10L)
                .role(Role.SHOP_OWNER)
                .build();
        Mockito.when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));
        //when
        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class,
                () -> productService.deleteProduct(wrongOwner, testProduct.getId()));
        //then
        assertThat(exception.getMessage()).isEqualTo("Access denied.");
    }
}