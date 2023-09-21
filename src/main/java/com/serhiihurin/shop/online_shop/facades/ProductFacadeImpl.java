package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.enums.SortingType;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.services.ProductService;
import com.serhiihurin.shop.online_shop.services.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductFacadeImpl implements ProductFacade {
    private final ProductService productService;
    private final ShopService shopService;

    @Override
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @Override
    public List<Product> getAllProductsByShopId(Long id) {
        return productService.getProductsByShopId(id);
    }

    @Override
    public List<Product> searchProducts(
            String productName, SortingType sortingType,
            Double minimalPrice, Double maximalPrice
    ) {
        if (minimalPrice != null && maximalPrice != null && maximalPrice < minimalPrice) {
            throw new ApiRequestException("Wrong price parameters values");
        }
        return productService.searchProducts(productName, sortingType, minimalPrice, maximalPrice);
    }

    @Override
    public Product getProduct(Long id) {
        return productService.getProduct(id);
    }

    @Override
    public Product addProduct(User currentAuthenticatedUser, ProductRequestDTO productRequestDTO) {
        Product product = productService.addProduct(
                Product.builder()
                .name(productRequestDTO.getName())
                .description(productRequestDTO.getDescription())
                .price(productRequestDTO.getPrice())
                .amount(productRequestDTO.getAmount())
                .shop(shopService.getShopByOwnerId(currentAuthenticatedUser.getId()))
                .build()
        );

        log.info("Added new product with id: {}", product.getId());
        return product;
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
}
