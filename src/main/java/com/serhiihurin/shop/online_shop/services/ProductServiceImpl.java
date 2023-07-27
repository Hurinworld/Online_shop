package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ProductRepository;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findProductsByProductDataId(Long id) {
        return productRepository.findProductsByProductDataId(id);
    }

    @Override
    public Product saveProduct(Product product) {
       return productRepository.save(product);
    }

    //TODO refactor
    @Override
    public Product getProduct(Long id) {
//        Optional<Product> optionalProduct = productRepository.findById(id);
//        if (optionalProduct.isEmpty()) {
//            throw new ApiRequestException("Could not find product");
//        }
//        return optionalProduct.get();
        return productRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Could not find product"));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
