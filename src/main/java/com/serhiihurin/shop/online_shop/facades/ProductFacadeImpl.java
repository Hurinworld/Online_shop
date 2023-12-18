package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dao.UserRepository;
import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.dto.SearchRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.facades.interfaces.ProductFacade;
import com.serhiihurin.shop.online_shop.services.interfaces.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
@Getter
public class ProductFacadeImpl implements ProductFacade {
    private final ProductService productService;
    private final ShopService shopService;
    private final ProductImageService productImageService;
    private final SearchService searchService;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${custom.image-retrieve-endpoint}")
    private String imageRetrieveEndpoint;

    public static Map<String, Long> userProductsAvailabilityTrackingList = new HashMap<>();

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
    public List<ProductResponseDTO> searchProductsGlobally(SearchRequestDTO searchRequestDTO) {
        checkPriceParameters(searchRequestDTO.getMinimalPrice(), searchRequestDTO.getMaximalPrice());

        List<ProductResponseDTO> productResponseDTOS = searchService.searchProductsGlobally(searchRequestDTO);

        productResponseDTOS
                .forEach(
                        productResponseDTO -> productResponseDTO
                                .setImagesEndpoints(getProductImages(productResponseDTO.getId()))
                );

        return productResponseDTOS;
    }

    @Override
    public List<ProductResponseDTO> searchProductsInShop(
            SearchRequestDTO searchRequestDTO) {
        checkPriceParameters(searchRequestDTO.getMinimalPrice(), searchRequestDTO.getMaximalPrice());

        List<ProductResponseDTO> productResponseDTOS = searchService.searchProductsInShop(searchRequestDTO);

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
            ProductRequestDTO productRequestDTO
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
    public ProductResponseDTO updateProduct(User currentAuthenticatedUser, Long id, ProductRequestDTO productRequestDTO) {
        Product oldProduct = productService.getProduct(id);

        List<User> productSubscriptionList = oldProduct.getProductAvailabilitySubscriptionList();

        if (oldProduct.getAmount() == 0 && productRequestDTO.getAmount() != null) {
            for (User user : productSubscriptionList) {
                emailService.addToProductAvailabilitySendingQueue(user.getEmail(), oldProduct);
                userService.unsubscribeFromNotificationAboutProductAvailability(oldProduct, user);
            }
        }

        ProductResponseDTO productResponseDTO = modelMapper.map(
                productService.updateProduct(
                        currentAuthenticatedUser,
                        productRequestDTO,
                        oldProduct
                ),
                ProductResponseDTO.class
        );

        productResponseDTO.setImagesEndpoints(getProductImages(productResponseDTO.getId()));

        log.info("Updated product with id: {}", id);

        return productResponseDTO;
    }

    @Override
    public void removeProductFromSale(User currentAuthenticatedUser, Long productId) {
        productService.removeProductFromSale(currentAuthenticatedUser, productId);
        log.info("Product with id: {} has been removed from sale", productId);
    }


    @Override
    public void deleteProduct(User currentAuthenticatedUser, Long productId) {
        log.info("Deleting product with id: {}", productId);
        productService.deleteProduct(currentAuthenticatedUser, productId);
    }

    @Override
    public void subscribeForNotificationAboutAvailability(Long productId, User currentAuthenticatedUser) {
        Product product = productService.getProduct(productId);
        userService.subscribeForNotificationAboutProductAvailability(product, currentAuthenticatedUser);

    }

    private List<String> getProductImages(Long productId) {
        return productImageService.getProductImagesByProductId(productId)
                .stream()
                .map(productImage -> imageRetrieveEndpoint + productImage.getImageInfo().getImageToken())
                .toList();
    }

    private void checkPriceParameters(Double minimalPrice, Double maximalPrice) {
        if (minimalPrice != null && maximalPrice != null && maximalPrice < minimalPrice) {
            throw new ApiRequestException("Wrong price parameters values");
        }
    }
}
