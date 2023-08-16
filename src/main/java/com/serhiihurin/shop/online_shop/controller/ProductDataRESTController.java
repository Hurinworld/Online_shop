package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.dto.ProductDataRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductDataResponseDTO;
import com.serhiihurin.shop.online_shop.facades.ProductDataFacade;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/products-data")
@PreAuthorize("hasAnyRole('ADMIN', 'SHOP_OWNER', 'CLIENT', 'SUPER_ADMIN')")
@RequiredArgsConstructor
public class ProductDataRESTController {
    private final ProductDataFacade productDataFacade;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('admin view info')")
    public List<ProductDataResponseDTO> getAllProductData() {
        return modelMapper.map(
                productDataFacade.getAllProductData(),
                new TypeToken<List<ProductDataResponseDTO>>() {
                }.getType()
        );
    }

    @GetMapping("/shop")
    @PreAuthorize("hasAnyAuthority('shop owner view info', 'admin view info')")
    List<ProductDataResponseDTO> getAllProductDataByShopId(@RequestParam Long id) {
        return modelMapper.map(
                productDataFacade.getAllProductDataByShopId(id),
                new TypeToken<List<ProductDataResponseDTO>>() {
                }.getType()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('shop owner view info', 'admin view info')")
    public ProductDataResponseDTO getProductData(@PathVariable Long id) {
        return modelMapper.map(productDataFacade.getProductData(id), ProductDataResponseDTO.class);
    }

    @GetMapping("/products")
    @PreAuthorize("hasAnyAuthority('shop owner view info', 'admin view info')")
    public List<ProductResponseDTO> getProductsByProductDataId(@RequestParam Long productDataId) {
        return modelMapper.map(
                productDataFacade.getProductsByProductDataId(productDataId),
                new TypeToken<List<ProductResponseDTO>>() {
                }.getType()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('product data management')")
    public ResponseEntity<ProductDataResponseDTO> addNewProductData
            (
                    @RequestParam Long shopId,
                    @RequestBody ProductDataRequestDTO productDataRequestDTO
            ) {
        return ResponseEntity.ok(
                modelMapper.map(
                        productDataFacade.addProductData(shopId, productDataRequestDTO),
                        ProductDataResponseDTO.class
                )
        );
    }

    //TODO add id to path! //done
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('product data management')")
    public ResponseEntity<ProductDataResponseDTO> updateProductData(
            @PathVariable Long id,
            @RequestBody ProductDataRequestDTO productDataRequestDTO
    ) {
        return ResponseEntity.ok(
                productDataFacade.updateProductData(id, productDataRequestDTO)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('product data management', 'super admin info deletion')")
    public ResponseEntity<Void> deleteProductData(@PathVariable Long id) {
        productDataFacade.deleteProductData(id);
        return ResponseEntity.ok().build();
    }
}
