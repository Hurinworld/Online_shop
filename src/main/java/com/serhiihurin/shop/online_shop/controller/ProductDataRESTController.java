package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.ProductDTO;
import com.serhiihurin.shop.online_shop.dto.ProductDataDTO;
import com.serhiihurin.shop.online_shop.entity.ProductData;
import com.serhiihurin.shop.online_shop.facades.ProductDataFacade;
import com.serhiihurin.shop.online_shop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public List<ProductDataDTO> getAllProductData() {
        return modelMapper.map(
                productDataFacade.getAllProductData(),
                new TypeToken<List<ProductDataDTO>>(){}.getType()
        );
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyAuthority('shop owner:read', 'admin:read')")
    List<ProductDataDTO> getAllProductDataByShopId(@PathVariable Long id) {
        return modelMapper.map(
                productDataFacade.getAllProductDataByShopId(id),
                new TypeToken<List<ProductDataDTO>>(){}.getType()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('shop owner:read', 'admin:read')")
    public ProductDataDTO getProductData(@PathVariable Long id) {
        return modelMapper.map(productDataFacade.getProductData(id), ProductDataDTO.class);
    }

    @GetMapping ("/products")
    @PreAuthorize("hasAnyAuthority('shop owner:read', 'admin:read')")
    public List<ProductDTO> getProductsByProductDataId(@RequestParam Long productDataId) {
        return modelMapper.map(
                productService.findProductsByProductDataId(productDataId),
                new TypeToken<List<ProductDTO>>(){}.getType()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('shop owner:create')")
    public ResponseEntity<ProductDataDTO> addNewProductData(@RequestParam Long shopId,
                                                         @RequestBody ProductData productData) {
        return ResponseEntity.ok(
                modelMapper.map(
                        productDataFacade.addProductData(shopId, productData),
                        ProductDataDTO.class
                )
        );
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
