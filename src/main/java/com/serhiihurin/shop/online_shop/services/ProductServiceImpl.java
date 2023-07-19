package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ProductRepository;
import com.serhiihurin.shop.online_shop.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public Product getProduct(Long id) {
        Product product = null;
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            product = optionalProduct.get();
        }
        return product;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
