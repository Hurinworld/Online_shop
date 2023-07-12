package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.facades.ProductFacade;
import com.serhiihurin.shop.online_shop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/products")
@PreAuthorize("hasAnyRole('ADMIN', 'SHOP_OWNER', 'CLIENT')")
@RequiredArgsConstructor
public class ProductRESTController {
    private final ProductFacade productFacade;
    private final ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('shop owner:create')")
    public ResponseEntity<Product> addNewProduct(@RequestParam Long productDataId, @RequestBody Product product) {
        return ResponseEntity.ok(productFacade.addProduct(productDataId,product));
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('shop owner:update')")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productFacade.updateProduct(product));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('shop owner:delete')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productFacade.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
