package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.services.ProductDataService;
import com.serhiihurin.shop.online_shop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductFacadeImpl implements ProductFacade {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDataService productDataService;

    @Override
    public Product addProduct(Long productDataId, Product product) {
        ProductData productData = productDataService.getProductData(productDataId);
        productData.setCount(productData.getCount() + 1);
        product.setProductData(productData);

        productService.saveProduct(product);
        return product;
    }

    @Override
    public Product updateProduct(Product product) {
        Product oldProduct = productService.getProduct(product.getId());
        ProductData productData = oldProduct.getProductData();

        product.setProductData(productData);
        productService.saveProduct(product);
        return product;
    }

    @Override
    public String deleteProduct(Long id) {
        Product product = productService.getProduct(id);
        ProductData productData = product.getProductData();
        productData.setCount(productData.getCount()-1);

        productDataService.saveProductData(productData);
        productService.deleteProduct(id);
        return "Product with id " + id + " was deleted";
    }
}
