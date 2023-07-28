package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ProductDataRepository;
import com.serhiihurin.shop.online_shop.dto.ProductDataRequestDTO;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductDataServiceImpl implements ProductDataService {
    private final ProductDataRepository productDataRepository;

    @Override
    public List<ProductData> getAllProductData() {
        return productDataRepository.findAll();
    }

    @Override
    public List<ProductData> getProductDataByShopId(Long id) {
        Optional<ProductData> optionalProductData = productDataRepository.findById(id);
        if (optionalProductData.isEmpty()) {
            throw new ApiRequestException("Could not find products data from this shop");
        }
        return productDataRepository.getProductDataByShopId(id);
    }

    @Override
    public ProductData getProductData(Long id) {
        return productDataRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Could not find data about product"));
    }

    @Override
    public ProductData saveProductData(ProductData productData) {
        return productDataRepository.save(productData);
    }

    @Override
    public ProductData updateProductData(
            ProductDataRequestDTO productDataRequestDTO,
            Shop shop,
            ProductData productData
    ) {
        if (shop != null) {
            productData.setShop(shop);
        }
        if (productDataRequestDTO.getName() != null) {
            productData.setName(productDataRequestDTO.getName());
        }
        if (productDataRequestDTO.getDescription() != null) {
            productData.setDescription(productDataRequestDTO.getDescription());
        }
        if (productDataRequestDTO.getPrice() != null) {
            productData.setPrice(productDataRequestDTO.getPrice());
        }

        return productDataRepository.save(productData);
    }


    @Override
    public void deleteProductData(Long id) {
        productDataRepository.deleteById(id);
    }
}
