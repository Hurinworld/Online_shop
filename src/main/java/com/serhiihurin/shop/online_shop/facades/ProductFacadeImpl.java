package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.ProductImage;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.enums.SortingType;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.services.ProductImageService;
import com.serhiihurin.shop.online_shop.services.ProductService;
import com.serhiihurin.shop.online_shop.services.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductFacadeImpl implements ProductFacade {
    private final ProductService productService;
    private final ShopService shopService;
    private final ProductImageService productImageService;
    private final ModelMapper modelMapper;

    @Value("${custom.files-saving-path}")
    private String fileSavingPath;

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<ProductResponseDTO> productResponseDTOS = modelMapper.map(
                productService.getAllProducts(),
                new TypeToken<List<ProductResponseDTO>>() {
                }.getType()
        );

        productResponseDTOS
                .forEach(
                        productResponseDTO -> productResponseDTO
                                .setImagesPaths(getProductImagesPaths(productResponseDTO.getId()))
                );

        return productResponseDTOS;
    }

    @Override
    public List<ProductResponseDTO> getAllProductsByShopId(Long id) {
        List<ProductResponseDTO> productResponseDTOS = modelMapper.map(
                productService.getProductsByShopId(id),
                new TypeToken<List<ProductResponseDTO>>() {
                }.getType()
        );

        productResponseDTOS
                .forEach(
                        productResponseDTO -> productResponseDTO
                                .setImagesPaths(getProductImagesPaths(productResponseDTO.getId()))
                );

        return productResponseDTOS;
    }

    @Override
    public List<ProductResponseDTO> searchProducts(
            String productName, SortingType sortingType,
            Double minimalPrice, Double maximalPrice
    ) {
        if (minimalPrice != null && maximalPrice != null && maximalPrice < minimalPrice) {
            throw new ApiRequestException("Wrong price parameters values");
        }
        List<ProductResponseDTO> productResponseDTOS = modelMapper.map(
                productService.searchProducts(productName, sortingType, minimalPrice, maximalPrice),
                new TypeToken<List<ProductResponseDTO>>() {
                }.getType()
        );

        productResponseDTOS
                .forEach(
                        productResponseDTO -> productResponseDTO
                                .setImagesPaths(getProductImagesPaths(productResponseDTO.getId()))
                );

        return productResponseDTOS;
    }

    @Override
    public ProductResponseDTO getProduct(Long id) {
        ProductResponseDTO productResponseDTO = modelMapper.map(
                productService.getProduct(id),
                ProductResponseDTO.class
        );
        productResponseDTO.setImagesPaths(getProductImagesPaths(id));
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO addProduct(
            User currentAuthenticatedUser,
            ProductRequestDTO productRequestDTO,
            MultipartFile[] files
    ) {
        Product product = productService.addProduct(
                Product.builder()
                .name(productRequestDTO.getName())
                .description(productRequestDTO.getDescription())
                .price(productRequestDTO.getPrice())
                .amount(productRequestDTO.getAmount())
                .shop(shopService.getShopByOwnerId(currentAuthenticatedUser.getId()))
                .build()
        );

        ProductResponseDTO productResponseDTO = modelMapper.map(product, ProductResponseDTO.class);

        List<String> filePaths = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String filePath = fileSavingPath + file.getOriginalFilename();
                    File destFile = new File(filePath);
                    file.transferTo(destFile);
                    filePaths.add(filePath);
                    productImageService.addProductImage(
                            ProductImage.builder()
                                    .filepath(filePath)
                                    .product(product)
                                    .build()
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        productResponseDTO.setImagesPaths(filePaths);

        log.info("Added new product with id: {}", product.getId());
        return productResponseDTO;
    }

    @Override
    public void putProductOnSale(User currentAuthenticatedUser, Long productId, int discountPercent) {
        productService.putProductOnSale(currentAuthenticatedUser, productId, discountPercent, null);
        log.info("Put product with id: {} on sale", productId);
    }

    @Override
    public Product updateProduct(User currentAuthenticatedUser, Long id, ProductRequestDTO productRequestDTO) {
        Product oldProduct = productService.getProduct(id);

        log.info("Updated product with id: {}", id);
        return productService.updateProduct(
                currentAuthenticatedUser,
                productRequestDTO,
                oldProduct
        );
    }

    @Override
    public void removeProductFromSale(User currentAuthenticatedUser, Long productId) {
        productService.removeProductFromSale(currentAuthenticatedUser, productId);
        log.info("Product with id: {} has been removed from sale", productId);
    }


    @Override
    public void deleteProduct(User currentAuthenticatedUser, Long id) {
        log.info("Deleting product with id: {}", id);
        productService.deleteProduct(currentAuthenticatedUser, id);
    }

    private List<String> getProductImagesPaths(Long productId) {
        return productImageService
                .getProductImagesByProductId(productId)
                .stream()
                .map(ProductImage::getFilepath)
                .toList();
    }
}
