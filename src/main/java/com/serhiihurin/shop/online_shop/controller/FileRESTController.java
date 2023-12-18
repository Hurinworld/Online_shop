package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.facades.interfaces.FileFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/online-shop/files")
@Tag(name = "File")
@RequiredArgsConstructor
public class FileRESTController {
    private final FileFacade fileFacade;

    @GetMapping("/{imageToken}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageToken) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(fileFacade.getImage(imageToken));

    }

    //TODO create endpoint for saving files that`ll return url to get-endpoint for retrieving //done
    //TODO change structure of work with files //done
    @PreAuthorize("hasAnyAuthority('account management', 'account creation')")
    @PostMapping("/new/user-account")
    public ResponseEntity<List<String>> addUserImages(
            User currentAuthenticatedUser,
            @RequestPart("files") MultipartFile[] files
    ) {
        return ResponseEntity.ok(fileFacade.saveUserImages(currentAuthenticatedUser.getId(), files));
    }

    @PreAuthorize("hasAuthority('product management')")
    @PostMapping("/new/product/{productId}")
    public ResponseEntity<List<String>> addProductImages(
            User currentAuthenticatedUser,
            @PathVariable Long productId,
            @RequestPart("files") MultipartFile[] files
    ) {
        return ResponseEntity.ok(fileFacade.saveProductImages(currentAuthenticatedUser.getId(), productId, files));
    }

    @PreAuthorize("hasAnyAuthority('account management', 'account creation')")
    @DeleteMapping("/user-account/image/{imageToken}")
    public ResponseEntity<Void> deleteUserImage(@PathVariable String imageToken, User currentAuthenticatedUser) {
        fileFacade.deleteUserImage(imageToken, currentAuthenticatedUser.getId());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('product management')")
    @DeleteMapping("/product/image/{imageToken}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable String imageToken, User currentAuthenticatedUser) {
        fileFacade.deleteProductImage(imageToken, currentAuthenticatedUser.getId());
        return ResponseEntity.ok().build();
    }
}
