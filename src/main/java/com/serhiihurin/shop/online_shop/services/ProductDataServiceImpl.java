package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ProductDataRepository;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductDataServiceImpl implements ProductDataService {

    @Autowired
    private ProductDataRepository productDataRepository;

    @Override
    public List<ProductData> getAllProductData() {
        return productDataRepository.findAll();
    }

    @Override
    public List<ProductData> getProductDataByShopId(Long id) {
        return productDataRepository.getProductDataByShopId(id);
    }

    @Override
    public ProductData getProductData(Long id) {
        ProductData productData = null;
        Optional<ProductData> optionalProductData = productDataRepository.findById(id);
        if (optionalProductData.isPresent()) {
            productData = optionalProductData.get();
        }
        return productData;
    }

    @Override
    public ProductData saveProductData(ProductData productData) {
        return productDataRepository.save(productData);
    }

    @Override
    public void deleteProductData(Long id) {
        productDataRepository.deleteById(id);
    }
}
