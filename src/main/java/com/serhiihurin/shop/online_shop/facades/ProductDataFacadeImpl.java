package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ProductDataRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductDataResponseDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.services.ProductDataService;
import com.serhiihurin.shop.online_shop.services.ProductService;
import com.serhiihurin.shop.online_shop.services.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductDataFacadeImpl implements ProductDataFacade {
    private final ProductDataService productDataService;
    private final ShopService shopService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Override
    public List<ProductData> getAllProductData() {
        return productDataService.getAllProductData();
    }

    @Override
    public List<ProductData> getAllProductDataByShopId(Long id) {
        return productDataService.getProductDataByShopId(id);
    }

    @Override
    public List<Product> getProductsByProductDataId(Long productDataId) {
        if(productDataService.getProductData(productDataId) == null) {
            throw new ApiRequestException("Could not find the data");
        }
        return productService.getProductsByProductDataId(productDataId);
    }

    @Override
    public ProductData getProductData(Long id) {
        return productDataService.getProductData(id);
    }

    @Override
    public ProductData addProductData(Long shopId, ProductDataRequestDTO productDataRequestDTO) {
        if (shopService.getShop(shopId) == null) {
            throw new ApiRequestException("Could not add product because shop doesn't exists");
        }

        Shop shop = shopService.getShop(shopId);

        ProductData productData = ProductData.builder()
                .name(productDataRequestDTO.getName())
                .description(productDataRequestDTO.getDescription())
                .price(productDataRequestDTO.getPrice())
                .count(productDataRequestDTO.getCount())
                .shop(shop)
                .build();

        ProductData savedProductData = productDataService.saveProductData(productData);

        IntStream.range(0, productData.getCount())
                .mapToObj(i -> {
                    Product product = new Product();
                    product.setProductData(productData);
                    return product;
                })
                .forEach(productService::saveProduct);

        log.info("Added new product data with id: {}", savedProductData.getId());
        return savedProductData;
    }

    @Override
    public ProductDataResponseDTO updateProductData(ProductDataRequestDTO productDataRequestDTO) {
        ProductData oldProductData = productDataService.getProductData(productDataRequestDTO.getId());
        Shop shop = null;
        if (productDataRequestDTO.getShopId() != null) {
            shop = shopService.getShop(productDataRequestDTO.getShopId());
        }

        ProductData productData = productDataService.updateProductData(
                productDataRequestDTO,
                shop,
                oldProductData
        );

        ProductDataResponseDTO productDataResponseDTO = modelMapper.map(
                productData,
                ProductDataResponseDTO.class
        );

        productDataResponseDTO.setShopId(productData.getShop().getId());

        log.info("Updated product data with id: {}", productDataRequestDTO.getId());
        return productDataResponseDTO;
    }

    @Override
    public void deleteProductData(Long id) {
        log.info("Deleting product data with id: {}", id);
        productDataService.deleteProductData(id);
    }
}
