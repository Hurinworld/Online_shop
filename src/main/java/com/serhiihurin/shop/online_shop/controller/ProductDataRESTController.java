package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.facades.ProductDataFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online_shop/products_data")
public class ProductDataRESTController {
    @Autowired
    private ProductDataFacade productDataFacade;

    @GetMapping
    public List<ProductData> showAllProductData() {
        return productDataFacade.showAllProductData();
    }

    @GetMapping("/get/{id}")
    List<ProductData> showAllProductDataByShopId(@PathVariable Long id) {
        return productDataFacade.showAllProductDataByShopId(id);
    }

    @GetMapping("/{id}")
    public ProductData showProductData(@PathVariable Long id) {
        return productDataFacade.showProductData(id);
    }

    @PostMapping("/{shopId}")
    public ProductData addNewProductData(@PathVariable Long shopId, @RequestBody ProductData productData) {
        return productDataFacade.addProductData(shopId, productData);
    }

    @PatchMapping
    public ProductData updateProductData(@RequestBody ProductData productData) {
        return productDataFacade.updateProductData(productData);
    }

    @DeleteMapping("/{id}")
    public String deleteProductData(@PathVariable Long id) {
        return productDataFacade.deleteProductData(id);
    }
}
