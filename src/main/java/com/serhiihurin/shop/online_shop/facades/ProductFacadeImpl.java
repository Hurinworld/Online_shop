package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductFacadeImpl implements ProductFacade {
    private final ProductService productService;
    private final ShopService shopService;
    private final ProductImageService productImageService;
    private final SearchService searchService;
    private final ModelMapper modelMapper;

    @Value("${custom.image-retrieve-endpoint}")
    private String imageRetrieveEndpoint;

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
                                .setImagesEndpoints(getProductImages(productResponseDTO.getId()))
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
                                .setImagesEndpoints(getProductImages(productResponseDTO.getId()))
                );

        return productResponseDTOS;
    }

    @Override
    public List<ProductResponseDTO> searchProductsGlobally(
            String productName,
            String sortingParameterValue,
            String sortingTypeValue,
            Double minimalPrice,
            Double maximalPrice
    ) {
        checkPriceParameters(minimalPrice, maximalPrice);

        List<ProductResponseDTO> productResponseDTOS = searchService.searchProductsGlobally(
                productName,
                sortingParameterValue,
                sortingTypeValue,
                minimalPrice,
                maximalPrice
        );

        productResponseDTOS
                .forEach(
                        productResponseDTO -> productResponseDTO
                                .setImagesEndpoints(getProductImages(productResponseDTO.getId()))
                );

        return productResponseDTOS;
    }

    @Override
    public List<ProductResponseDTO> searchProductsInShop(
            User currentAuthenticatedUser,
            String productName,
            String sortingParameterValue,
            String sortingTypeValue,
            Double minimalPrice,
            Double maximalPrice
    ) {
        checkPriceParameters(minimalPrice, maximalPrice);

        List<ProductResponseDTO> productResponseDTOS = searchService.searchProductsInShop(
                currentAuthenticatedUser,
                productName,
                sortingParameterValue,
                sortingTypeValue,
                minimalPrice,
                maximalPrice
        );

        productResponseDTOS
                .forEach(
                        productResponseDTO -> productResponseDTO
                                .setImagesEndpoints(getProductImages(productResponseDTO.getId()))
                );

        return  productResponseDTOS;
    }

    @Override
    public ProductResponseDTO getProduct(Long id) {
        ProductResponseDTO productResponseDTO = modelMapper.map(
                productService.getProduct(id),
                ProductResponseDTO.class
        );
        productResponseDTO.setImagesEndpoints(getProductImages(id));
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO addProduct(
            User currentAuthenticatedUser,
            ProductRequestDTO productRequestDTO,
            MultipartFile[] files
    ) {
        productRequestDTO.setShopId(
                shopService.getShopByOwnerId(currentAuthenticatedUser.getId())
                        .getId()
        );
        ProductResponseDTO productResponseDTO = modelMapper.map(
                productService.addProduct(productRequestDTO),
                ProductResponseDTO.class
        );

        log.info("Added new product with id: {}", productResponseDTO.getId());
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

    private List<String> getProductImages(Long productId) {
        return productImageService.getProductImagesByProductId(productId)
                .stream()
                .map(productImage -> imageRetrieveEndpoint + productImage.getToken())
                .toList();
    }

    private void checkPriceParameters(Double minimalPrice, Double maximalPrice) {
        if (minimalPrice != null && maximalPrice != null && maximalPrice < minimalPrice) {
            throw new ApiRequestException("Wrong price parameters values");
        }
    }
}
