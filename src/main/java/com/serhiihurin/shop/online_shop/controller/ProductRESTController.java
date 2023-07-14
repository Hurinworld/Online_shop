package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ProductDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.facades.ProductFacade;
import com.serhiihurin.shop.online_shop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
    private final ModelMapper modelMapper;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return modelMapper.map(
                productService.getAllProducts(),
                new TypeToken<List<ProductDTO>>(){}.getType()
        );
    }

    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable Long id) {
        return modelMapper.map(productService.getProduct(id), ProductDTO.class);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('shop owner:create')")
    public ResponseEntity<ProductDTO> addNewProduct(@RequestParam Long productDataId, @RequestBody Product product) {
        return ResponseEntity.ok(
                modelMapper.map(
                        productFacade.addProduct(productDataId,product),
                        ProductDTO.class
                )
        );
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
