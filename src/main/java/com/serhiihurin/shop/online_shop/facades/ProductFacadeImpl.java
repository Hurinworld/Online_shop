package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.ProductImage;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.enums.Role;
import com.serhiihurin.shop.online_shop.enums.SortingType;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.exception.UnauthorizedAccessException;
import com.serhiihurin.shop.online_shop.services.FileService;
import com.serhiihurin.shop.online_shop.services.ProductImageService;
import com.serhiihurin.shop.online_shop.services.ProductService;
import com.serhiihurin.shop.online_shop.services.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductFacadeImpl implements ProductFacade {
    private final ProductService productService;
    private final ShopService shopService;
    private final ProductImageService productImageService;
    private final FileService fileService;
    private final ModelMapper modelMapper;

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
    public List<ProductResponseDTO> searchProductsGlobally(
            String productName, SortingType sortingType,
            Double minimalPrice, Double maximalPrice
    ) {
        List<Product> productList = productService.getAllProducts();

        return performSearch(productList, productName,sortingType, minimalPrice, maximalPrice);
    }

    @Override
    public List<ProductResponseDTO> searchProductsInShop(
            User currentAuthenticatedUser,
            String productName,
            SortingType sortingType,
            Double minimalPrice,
            Double maximalPrice
    ) {
        if (!Role.SHOP_OWNER.equals(currentAuthenticatedUser.getRole())) {
            throw new UnauthorizedAccessException("Access denied");
        }
        List<Product> productList = productService.getProductsByShopId(
                shopService.getShopByOwnerId(currentAuthenticatedUser.getId()).getId()
        );
        return performSearch(productList, productName,sortingType, minimalPrice, maximalPrice);
    }

    @Override
    public ProductResponseDTO getProduct(Long id) {
        ProductResponseDTO productResponseDTO = modelMapper.map(
                productService.getProduct(id),
                ProductResponseDTO.class
        );
        productResponseDTO.setImages(getProductImages(id));
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
        //TODO better use dto to transfer data between layers //done
        ProductResponseDTO productResponseDTO = modelMapper.map(
                productService.addProduct(productRequestDTO),
                ProductResponseDTO.class
        );

        //TODO extract this logic to the file-service //done
        productResponseDTO.setImages(fileService.saveProductImages(productResponseDTO.getId(), files));

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

    private List<String> getProductImagesPaths(Long productId) {
        return productImageService
                .getProductImagesByProductId(productId)
                .stream()
                .map(ProductImage::getFilepath)
                .toList();
    }

    private List<byte[]> getProductImages(Long productId) {
        return productImageService.getProductImagesByProductId(productId)
                .stream()
                .map(productImage -> {
                    try {
                        return Files.readAllBytes(Path.of(productImage.getFilepath()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }

    private List<ProductResponseDTO> performSearch(
            List<Product> productList,
            String productName,
            SortingType sortingType,
            Double minimalPrice,
            Double maximalPrice
    ) {
        //TODO wrong check
        if (productName == null) {
            return modelMapper.map(
                    productList,
                    new TypeToken<List<ProductResponseDTO>>() {
                    }.getType()
            );
        }

        if (minimalPrice != null && maximalPrice != null && maximalPrice < minimalPrice) {
            throw new ApiRequestException("Wrong price parameters values");
        }

        List<ProductResponseDTO> productResponseDTOS =
                productService.searchProducts(productList, productName, sortingType, minimalPrice, maximalPrice);

        productResponseDTOS
                .forEach(
                        productResponseDTO -> productResponseDTO
                                .setImagesPaths(getProductImagesPaths(productResponseDTO.getId()))
                );

        return productResponseDTOS;
    }
}
