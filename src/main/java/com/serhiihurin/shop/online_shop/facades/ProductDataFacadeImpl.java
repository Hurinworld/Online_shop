package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ProductDataRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductDataResponseDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.services.ProductDataService;
import com.serhiihurin.shop.online_shop.services.ProductService;
import com.serhiihurin.shop.online_shop.services.ShopService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
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
        return productService.findProductsByProductDataId(productDataId);
    }

    @Override
    public ProductData getProductData(Long id) {
        return productDataService.getProductData(id);
    }

    @Override
    public ProductData addProductData(Long shopId, ProductDataRequestDTO productDataRequestDTO) {
        Shop shop = shopService.getShop(shopId);

        ProductData productData = ProductData.builder()
                .name(productDataRequestDTO.getName())
                .description(productDataRequestDTO.getDescription())
                .price(productDataRequestDTO.getPrice())
                .count(productDataRequestDTO.getCount())
                .build();

        productData.setShop(shop);
        ProductData savedProductData = productDataService.saveProductData(productData);

        IntStream.range(0, productData.getCount())
                .mapToObj(i -> {
                    Product product = new Product();
                    product.setProductData(productData);
                    return product;
                })
                .forEach(productService::saveProduct);

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

        return productDataResponseDTO;
    }

    @Override
    public void deleteProductData(Long id) {
        productDataService.deleteProductData(id);
    }
}
