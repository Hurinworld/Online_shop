package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.services.ProductDataService;
import com.serhiihurin.shop.online_shop.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDataFacadeImpl implements ProductDataFacade{
    @Autowired
    private ProductDataService productDataService;

    @Autowired
    private ShopService shopService;

    @Override
    public List<ProductData> showAllProductData() {
        return productDataService.getAllProductData();
    }

    @Override
    public List<ProductData> showAllProductDataByShopId(Long id) {
        return productDataService.getProductDataByShopId(id);
    }

    @Override
    public ProductData showProductData(Long id) {
        return productDataService.getProductData(id);
    }

    @Override
    public ProductData addProductData(Long shopId, ProductData productData) {
        Shop shop = shopService.getShop(shopId);
        productData.setShop(shop);
        productDataService.saveProductData(productData);
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
