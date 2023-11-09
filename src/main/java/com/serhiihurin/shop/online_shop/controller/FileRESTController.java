package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.facades.FileFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                .body(fileFacade.getProductImage(imageToken));
    }

    //TODO create endpoint for saving files that`ll return url to get-endpoint for retrieving
    //TODO change structure of work with files
}
