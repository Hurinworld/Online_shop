package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.facades.ProductDataFacade;
import com.serhiihurin.shop.online_shop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/products-data")
@PreAuthorize("hasAnyRole('ADMIN', 'SHOP_OWNER', 'CLIENT')")
@RequiredArgsConstructor
public class ProductDataRESTController {
    private final ProductDataFacade productDataFacade;
    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public List<ProductData> getAllProductData() {
        return productDataFacade.getAllProductData();
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyAuthority('shop owner:read', 'admin:read')")
    List<ProductData> getAllProductDataByShopId(@PathVariable Long id) {
        return productDataFacade.getAllProductDataByShopId(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('shop owner:read', 'admin:read')")
    public ProductData getProductData(@PathVariable Long id) {
        return productDataFacade.getProductData(id);
    }

    @GetMapping ("/products")
    @PreAuthorize("hasAnyAuthority('shop owner:read', 'admin:read')")
    public List<Product> getProductsByProductDataId(@RequestParam Long productDataId) {
        return productService.findProductsByProductDataId(productDataId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('shop owner:create')")
    public ResponseEntity<ProductData> addNewProductData(@RequestParam Long shopId, @RequestBody ProductData productData) {
        return ResponseEntity.ok(productDataFacade.addProductData(shopId, productData));
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('shop owner:update')")
    public ResponseEntity<ProductData> updateProductData(@RequestBody ProductData productData) {
        return ResponseEntity.ok(productDataFacade.updateProductData(productData));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('shop owner:delete')")
    public ResponseEntity<Void> deleteProductData(@PathVariable Long id) {
        productDataFacade.deleteProductData(id);
        return ResponseEntity.ok().build();
    }
}
