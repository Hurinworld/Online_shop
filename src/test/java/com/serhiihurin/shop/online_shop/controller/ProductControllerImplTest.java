package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.config.SecurityConfig;
import com.serhiihurin.shop.online_shop.dao.ProductRepository;
import com.serhiihurin.shop.online_shop.dao.ShopRepository;
import com.serhiihurin.shop.online_shop.dao.UserRepository;
import com.serhiihurin.shop.online_shop.dto.ApiExceptionDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.enums.Role;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
//@WithSecurityContext()
class ProductControllerImplTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ModelMapper modelMapper;
    private Product testProduct;
    private User testUser;
    private Shop testShop;

    static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mySQLContainer.stop();
    }

    @BeforeEach
    public void setUp() {
//        modelMapper = new ModelMapper();
//        modelMapper.getConfiguration()
//                .setMatchingStrategy(MatchingStrategies.LOOSE);
        testUser = User.builder()
//                .id(1L)
                .firstName("User")
                .lastName("For test")
                .email("testUser@gmail.com")
                .password("password")
                .cash(26665.0)
                .role(Role.SHOP_OWNER)
                .build();
        testShop = Shop.builder()
//                .id(1L)
                .name("Test shop")
                .income(325665.0)
                .owner(testUser)
                .build();
        testProduct = Product.builder()
//                .id(1L)
                .name("AMD Ryzen 7 5700X")
                .description("CPU")
                .amount(100)
                .price(8500.0)
                .shop(testShop)
                .build();
        userRepository.save(testUser);
        shopRepository.save(testShop);
        productRepository.save(testProduct);
    }

    @AfterEach
    public void afterEach() {
        productRepository.deleteAll();
        shopRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
//    @WithUserDetails(userDetailsServiceBeanName = "userDetailsService", value = "testUser@gmail.com")
//    @WithMockUser(username = "testUser@gmail.com", roles = {"SHOP_OWNER"})
    void getProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/online-shop/products/" + testProduct.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                .accept("application/json")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testProduct.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(testProduct.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(testProduct.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shopId").value(testProduct.getShop().getId()));
    }

    @Test
    @Disabled
    void addNewProduct() {
    }

    @Test
    @Disabled
    void updateProduct() {
    }

    @Test
    void deleteProduct() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/online-shop/products/" + testProduct.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                .accept("application/json")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").doesNotExist());
        Optional<Product> deletedProduct = productRepository.getProductByName("AMD Ryzen 7 5700X");
        Assertions.assertTrue(deletedProduct.isEmpty());
    }

//    @Test
//    void deleteProductWithWrongAuthoritiesThrowsException() throws Exception{
//        testUser.setRole(Role.CLIENT);
//        userRepository.save(testUser);
//        mockMvc.perform(MockMvcRequestBuilders
//                        .delete("/online-shop/products/" + testProduct.getId())
//                        .with(SecurityMockMvcRequestPostProcessors.user(testUser))
//                        .accept("application/json")
//                        .contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
////                .andExpect(MockMvcResultMatchers.jsonPath("$.*").value(ApiExceptionDTO.class))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Access denied."));
//        Optional<Product> deletedProduct = productRepository.getProductByName("AMD Ryzen 7 5700X");
//        Assertions.assertTrue(deletedProduct.isPresent());
//    }
}