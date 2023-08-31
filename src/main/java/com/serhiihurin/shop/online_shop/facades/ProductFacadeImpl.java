package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.services.ProductService;
import com.serhiihurin.shop.online_shop.services.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductFacadeImpl implements ProductFacade {
    private final ProductService productService;
    private final ShopService shopService;
    private final ModelMapper modelMapper;

    @Override
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @Override
    public List<Product> getAllProductsByShopId(Long id) {
        return productService.getProductsByShopId(id);
    }

    @Override
    public Product getProduct(Long id) {
        return productService.getProduct(id);
    }

    @Override
    public Product addProduct(Long shopId, ProductRequestDTO productRequestDTO) {
        if (shopService.getShop(shopId) == null) {
            throw new ApiRequestException("Could not add product because shop doesn't exists");
        }

        Shop shop = shopService.getShop(shopId);

        Product product = Product.builder()
                .name(productRequestDTO.getName())
                .description(productRequestDTO.getDescription())
                .price(productRequestDTO.getPrice())
                .amount(productRequestDTO.getAmount())
                .shop(shop)
                .build();

        Product savedProduct = productService.saveProduct(product);

        log.info("Added new product with id: {}", savedProduct.getId());
        return savedProduct;
    }

    @Override
    public void increaseProductAmount(Long productId, Integer amount) {
        if (amount == null || amount < 0){
            throw new ApiRequestException("Wrong amount parameter");
        }
        productService.increaseProductAmount(
                productService.getProduct(productId),
                amount
        );
        log.info("Increased amount of product with ID: {} by {} units", productId, amount);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Product oldProduct = productService.getProduct(id);
        Shop shop = null;
        if (productRequestDTO.getShopId() != null) {
            shop = shopService.getShop(productRequestDTO.getShopId());
        }

        Product product = productService.updateProduct(
                productRequestDTO,
                shop,
                oldProduct
        );

        ProductResponseDTO productResponseDTO = modelMapper.map(
                product,
                ProductResponseDTO.class
        );

        productResponseDTO.setShopId(product.getShop().getId());

        log.info("Updated product with id: {}", id);
        return productResponseDTO;
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        productService.deleteProduct(id);
    }
}
