package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.services.ProductDataService;
import com.serhiihurin.shop.online_shop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductFacadeImpl implements ProductFacade {
    private final ProductService productService;
    private final ProductDataService productDataService;

    @Override
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @Override
    public Product getProduct(Long id) {
        return productService.getProduct(id);
    }


    @Override
    public Product addProduct(ProductRequestDTO productRequestDTO) {
        ProductData productData = productDataService.getProductData(productRequestDTO.getProductDataId());
        Product product = new Product();
        product.setProductData(productData);
        productData.setCount(productData.getCount() + 1);

        return productService.saveProduct(product);
    }

    @Override
    public Product updateProduct(ProductUpdateRequestDTO productUpdateRequestDTO) {
        Product product = productService.getProduct(productUpdateRequestDTO.getId());

        if (productUpdateRequestDTO.getProductDataId() != null) {
            product.setProductData(
                    productDataService.getProductData(
                            productUpdateRequestDTO.getProductDataId()
                    )
            );
        }

        return productService.saveProduct(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productService.getProduct(id);
        ProductData productData = product.getProductData();
        productData.setCount(productData.getCount() - 1);

        productDataService.saveProductData(productData);
        productService.deleteProduct(id);
    }
}
