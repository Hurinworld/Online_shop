package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.dto.ProductUpdateRequestDTO;
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
@PreAuthorize("hasAnyRole('ADMIN', 'SHOP_OWNER', 'CLIENT', 'SUPER_ADMIN')")
@RequiredArgsConstructor
public class ProductRESTController {
    private final ProductFacade productFacade;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return modelMapper.map(
                productService.getAllProducts(),
                new TypeToken<List<ProductResponseDTO>>(){}.getType()
        );
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProduct(@PathVariable Long id) {
        return modelMapper.map(productService.getProduct(id), ProductResponseDTO.class);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('product management')")
    public ResponseEntity<ProductResponseDTO> addNewProduct(
            @RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(
                modelMapper.map(
                        productFacade.addProduct(productRequestDTO),
                        ProductResponseDTO.class
                )
        );
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('product management')")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @RequestBody ProductUpdateRequestDTO productUpdateRequestDTO
    ) {
        return ResponseEntity.ok(
                modelMapper.map(
                        productFacade.updateProduct(productUpdateRequestDTO),
                        ProductResponseDTO.class
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('product management', 'super admin info deletion')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productFacade.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
