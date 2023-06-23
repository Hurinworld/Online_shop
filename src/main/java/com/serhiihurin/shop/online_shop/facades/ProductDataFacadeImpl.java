package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.services.ProductDataService;
import com.serhiihurin.shop.online_shop.services.ProductService;
import com.serhiihurin.shop.online_shop.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDataFacadeImpl implements ProductDataFacade {
    @Autowired
    private ProductDataService productDataService;

    @Autowired
    private ShopService shopService;

    @Autowired
    ProductService productService;

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
        Product product;
        Shop shop = shopService.getShop(shopId);
        productData.setShop(shop);
        productDataService.saveProductData(productData);

//        for (int i = 0; i < productData.getCount(); i++) {
//            productService.saveProduct();
//        }

        return productData;
    }

    @Override
    public ProductData updateProductData(ProductData productData) {
        ProductData oldProductData = productDataService.getProductData(productData.getId());
        Shop shop = oldProductData.getShop();
        productData.setShop(shop);

        productDataService.saveProductData(productData);
        return productData;
    }

    @Override
    public String deleteProductData(Long id) {
        productDataService.deleteProductData(id);
        return "Product data with id = " + id + " was deleted";
    }
}
