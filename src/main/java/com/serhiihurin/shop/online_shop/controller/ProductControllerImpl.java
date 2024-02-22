package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.controller.interfaces.ProductController;
import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.facades.interfaces.FileFacade;
import com.serhiihurin.shop.online_shop.facades.interfaces.ProductFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/online-shop/products")
@PreAuthorize("hasAnyRole('ADMIN', 'SHOP_OWNER', 'CLIENT', 'SUPER_ADMIN')")
@Tag(name = "Product")
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {
    private final ProductFacade productFacade;
    private final FileFacade fileFacade;

    @GetMapping
    @PreAuthorize("hasAuthority('admin view info')")
    public List<ProductResponseDTO> getAllProducts() {
        return productFacade.getAllProducts();
    }

    @GetMapping("/shop/{id}")
    @PreAuthorize("hasAnyAuthority('shop owner view info', 'admin view info')")
    public List<ProductResponseDTO> getAllProductsByShopId(@PathVariable Long id) {
        return productFacade.getAllProductsByShopId(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('shop owner view info', 'admin view info', 'client view info')")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productFacade.getProduct(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('product management')")
    public ResponseEntity<ProductResponseDTO> addNewProduct(
            User currentAuthenticatedUser,
            @RequestPart("product-request-dto") ProductRequestDTO productRequestDTO,
            @RequestPart("files")MultipartFile[] files
            ) {
        ProductResponseDTO productResponseDTO = productFacade.addProduct(
                currentAuthenticatedUser, productRequestDTO
        );
        productResponseDTO.setImagesEndpoints(fileFacade.saveProductImages(
                currentAuthenticatedUser.getId(),
                productResponseDTO.getId(),
                files
        ));

        return ResponseEntity.ok(productResponseDTO);
    }

    @PostMapping("/{productId}/sale")
    @PreAuthorize("hasAuthority('product management')")
    public ResponseEntity<Void> putProductOnSale(
            User currentAuthenticatedUser, @PathVariable Long productId, @RequestParam int discountPercent
    ) {
        productFacade.putProductOnSale(currentAuthenticatedUser, productId, discountPercent);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{productId}")
    @PreAuthorize("hasAuthority('product management')")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            User currentAuthenticatedUser,
            @PathVariable Long productId,
            @RequestBody ProductRequestDTO productRequestDTO
    ) {
        return ResponseEntity.ok(
                productFacade.updateProduct(currentAuthenticatedUser, productId, productRequestDTO)
        );
    }

    @DeleteMapping("/{productId}/sale")
    @PreAuthorize("hasAuthority('product management')")
    public ResponseEntity<Void> removeProductFromSale(User currentAuthenticatedUser, @PathVariable Long productId) {
        productFacade.removeProductFromSale(currentAuthenticatedUser, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('product management', 'super admin info deletion')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId, User currentAuthenticatedUser) {
        productFacade.deleteProduct(currentAuthenticatedUser, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/availability/{productId}")
    public ResponseEntity<Void> subscribeForNotificationAboutAvailability(
            @PathVariable Long productId,
            User currentAuthenticatedUser
    ) {
        productFacade.subscribeForNotificationAboutAvailability(productId, currentAuthenticatedUser);
        return ResponseEntity.ok().build();
    }
}
