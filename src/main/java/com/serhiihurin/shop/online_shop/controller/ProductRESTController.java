package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.facades.ProductFacade;
import com.serhiihurin.shop.online_shop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online_shop/products")
public class ProductRESTController {
    @Autowired
    private ProductFacade productFacade;
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @GetMapping ("/by_data")
    public List<Product> getProductsByProductDataId(@RequestParam Long productDataId) {
        return productService.findProductsByProductDataId(productDataId);
    }

    @PostMapping
    public Product addNewProduct(@RequestParam Long productDataId, @RequestBody Product product) {
        return productFacade.addProduct(productDataId,product);
    }

    @PatchMapping
    public Product updateProduct(@RequestBody Product product) {
        return productFacade.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public String deleteFeedback(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product with id " + id + " was deleted";
    }
}
