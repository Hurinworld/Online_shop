package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.services.ProductDataService;
import com.serhiihurin.shop.online_shop.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online_shop/products_data")
public class ProductDataRESTController {
    @Autowired
    private ProductDataService productDataService;

    @Autowired
    private ShopService shopService;

    @GetMapping
    public List<ProductData> showAllProductData() {
        return productDataService.getAllProductData();
    }

    @GetMapping("/get/{id}")
    List<ProductData> showAllProductDataByShopId(@PathVariable Long id) {
        return productDataService.getProductDataByShopId(id);
    }

    @GetMapping("/{id}")
    public ProductData showProductData(@PathVariable Long id) {
        return productDataService.getProductData(id);
    }

    @PostMapping("/{shopId}")
    public ProductData addNewProductData(@PathVariable Long shopId, @RequestBody ProductData productData) {
        Shop shop = shopService.getShop(shopId);
        productData.setShop(shop);
        productDataService.saveProductData(productData);
        return productData;
    }

    @PutMapping
    public ProductData updateProductData(@RequestBody ProductData productData) {
        productDataService.saveProductData(productData);
        return productData;
    }

    @DeleteMapping("/{id}")
    public String deleteProductData(@PathVariable Long id) {
        productDataService.deleteProductData(id);
        return "Product data with id = " + id + " was deleted";
    }
}
