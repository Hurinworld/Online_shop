package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.services.ProductDataService;
import com.serhiihurin.shop.online_shop.services.ProductService;
import com.serhiihurin.shop.online_shop.services.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class ProductDataFacadeImpl implements ProductDataFacade {
    private final ProductDataService productDataService;
    private final ShopService shopService;
    private final ProductService productService;

    @Override
    public List<ProductData> getAllProductData() {
        return productDataService.getAllProductData();
    }

    @Override
    public List<ProductData> getAllProductDataByShopId(Long id) {
        return productDataService.getProductDataByShopId(id);
    }

    @Override
    public ProductData getProductData(Long id) {
        return productDataService.getProductData(id);
    }

    @Override
    public ProductData addProductData(Long shopId, ProductData productData) {
        Shop shop = shopService.getShop(shopId);

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
    public ProductData updateProductData(ProductData productData) {
        ProductData oldProductData = productDataService.getProductData(productData.getId());
        Shop shop = oldProductData.getShop();
        productData.setShop(shop);

        return productDataService.saveProductData(productData);
    }

    @Override
    public void deleteProductData(Long id) {
        productDataService.deleteProductData(id);
    }
}
