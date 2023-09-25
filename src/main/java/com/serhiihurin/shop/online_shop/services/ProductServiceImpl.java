package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.*;
import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.entity.*;
import com.serhiihurin.shop.online_shop.enums.Role;
import com.serhiihurin.shop.online_shop.enums.SortingType;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ShopRepository shopRepository;
    private final DiscountRepository discountRepository;
    private final EventRepository eventRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByShopId(Long id) {
        shopRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Could not find shop with ID: " + id));
        //TODO add check if shop exist //done
        return productRepository.getProductsByShopId(id);
    }

    @Override
    public List<Product> searchProducts(
            String productName, SortingType sortingType, Double minimalPrice, Double maximalPrice
    ) {
        if (sortingType == null) {
            return searchProductsWithNoSearchingType(productName, minimalPrice, maximalPrice);
        } else if (sortingType.equals(SortingType.ASCENDING)) {
            return searchProductsWithAscendingSearchingType(productName, minimalPrice, maximalPrice);
        } else if (sortingType.equals(SortingType.DESCENDING)) {
            return searchProductsWithDescendingSearchingType(productName, minimalPrice, maximalPrice);
        } else {
           throw new ApiRequestException("Wrong sorting type parameter value");
        }
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Could not find product with ID: " + id));
    }

    @Override
    public Product addProduct(Product product) {

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(
            User currentAuthenticatedUser,
            ProductRequestDTO productRequestDTO,
            Product product
    ) {
        if (!product.getShop().getOwner().getId().equals(currentAuthenticatedUser.getId())) {
            throw new UnauthorizedAccessException("Access denied. Wrong product ID");
        }

        //TODO wrong logic //done //removed setting a shop to product

        if (productRequestDTO.getName() != null) {
            product.setName(productRequestDTO.getName());
        }
        if (productRequestDTO.getDescription() != null) {
            product.setDescription(productRequestDTO.getDescription());
        }
        if (productRequestDTO.getPrice() != null) {
            product.setPrice(productRequestDTO.getPrice());
        }
        if (productRequestDTO.getAmount() != null) {
            product.setAmount(productRequestDTO.getAmount());
        }

        return productRepository.save(product);
    }

    @Override
    public void putProductOnSale(User currentAuthenticatedUser, Object productSearchValue,
                                     int discountPercent, Event event) {
        Product product = getProductByProductSearchValue(productSearchValue);

        if(currentAuthenticatedUser.getRole().equals(Role.SHOP_OWNER)) {
            if (!product.getShop().getOwner().getId().equals(currentAuthenticatedUser.getId())) {
                throw new UnauthorizedAccessException("Access denied. Wrong product ID");
            }
        }

        if (discountPercent > 100 || discountPercent < 1) {
            throw new ApiRequestException("Invalid discount percent. " +
                    "The value should be higher than 1 and lower than 100");
        }

        if (product.isOnSale()) {
            throw new ApiRequestException("The product is already on sale");
        }

        product.setOnSale(true);
        product.setPrice(product.getPrice() - (product.getPrice() * discountPercent / 100));

        discountRepository.save(
                Discount.builder()
                        .product(product)
                        .event(event)
                        .discountPercent(discountPercent)
                        .build()
        );
        productRepository.save(product);
    }

    private Product getProductByProductSearchValue(Object productSearchValue) {
        if (productSearchValue instanceof Long) {
            return productRepository.findById((Long) productSearchValue)
                    .orElseThrow(() -> new ApiRequestException("Could not find product with ID: "
                            + productSearchValue));
        } else if (productSearchValue instanceof String) {
            return productRepository.getProductByName((String) productSearchValue)
                    .orElseThrow(() -> new ApiRequestException("Could not find product with name: "
                            + productSearchValue));
        } else {
            throw new ApiRequestException("Wrong parameter value "
                    + productSearchValue + ". The value should be of type Long or String");
        }
    }

    @Override
    public void removeProductFromSale(User currentAuthenticatedUser, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiRequestException("Could not find product with ID: " + productId));

        if (!product.getShop().getOwner().getId().equals(currentAuthenticatedUser.getId())) {
            throw new UnauthorizedAccessException("Access denied. Wrong product ID");
        }

        Discount discount = discountRepository.findByProductId(productId)
                .orElseThrow(() -> new ApiRequestException("The product has no discount"));

        product.setOnSale(false);
        product.setPrice(product.getPrice() / (1.0 - (discount.getDiscountPercent() / 100.0)));

        discountRepository.deleteById(discount.getId());
        productRepository.save(product);
    }

    @Override
    public void removeEventProductsFromSale(Long eventId) {
        //TODO remove this map //done
        eventRepository.findById(eventId)
                .orElseThrow(() -> new ApiRequestException("Could not find event with ID: " + eventId));
        List<Discount> eventProductsDiscounts = discountRepository.findDiscountsByEventId(eventId);

        eventProductsDiscounts.forEach(
                discount -> {
                    Product product = discount.getProduct();
                    product.setOnSale(false);
                    product.setPrice(product.getPrice() / (1.0 - (discount.getDiscountPercent() / 100.0)));
                    productRepository.save(product);
                }
        );

        discountRepository.deleteAll(eventProductsDiscounts);
    }

    @Transactional
    @Override
    public void deleteProduct(User currentAuthenticatedUser, Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Could not find product with ID: " + id));
        if (!product.getShop().getOwner().getId().equals(currentAuthenticatedUser.getId())) {
            throw new UnauthorizedAccessException("Access denied.");
        }
        List<ProductImage> productImages = productImageRepository.getProductImagesByProductId(id);
        productImages.forEach(productImage -> productImageRepository.deleteById(productImage.getId()));
        productRepository.deleteById(id);
    }

    private List<Product> searchProductsWithNoSearchingType(
            String productName, Double minimalPrice, Double maximalPrice
    ) {
        if (minimalPrice != null && maximalPrice != null) {
            return productRepository
                    .getProductsByNameContainsAndPriceBetween(productName, minimalPrice, maximalPrice);
        } else if(minimalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceGreaterThan(productName, minimalPrice);
        } else if(maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceLessThan(productName, maximalPrice);
        } else {
            return productRepository.getProductsByNameContains(productName);
        }
    }

    private List<Product> searchProductsWithAscendingSearchingType(
            String productName, Double minimalPrice, Double maximalPrice
    ){
        if (minimalPrice != null && maximalPrice != null) {
            return productRepository
                    .getProductsByNameContainsAndPriceBetweenOrderByPriceAsc(
                            productName, minimalPrice, maximalPrice
                    );
        } else if(minimalPrice != null) {
            return productRepository
                    .getProductsByNameContainsAndPriceGreaterThanOrderByPriceAsc(productName, minimalPrice);
        } else if(maximalPrice != null) {
            return productRepository
                    .getProductsByNameContainsAndPriceLessThanOrderByPriceAsc(productName, maximalPrice);
        } else {
            return productRepository.getProductsByNameContainsOrderByPriceAsc(productName);
        }
    }

    private List<Product> searchProductsWithDescendingSearchingType(
            String productName, Double minimalPrice, Double maximalPrice
    ) {
        if (minimalPrice != null && maximalPrice != null) {
            return productRepository
                    .getProductsByNameContainsAndPriceBetweenOrderByPriceDesc(
                            productName, minimalPrice, maximalPrice
                    );
        } else if(minimalPrice != null) {
            return productRepository
                    .getProductsByNameContainsAndPriceGreaterThanOrderByPriceDesc(productName, minimalPrice);
        } else if(maximalPrice != null) {
            return productRepository
                    .getProductsByNameContainsAndPriceLessThanOrderByPriceDesc(productName, maximalPrice);
        } else {
            return productRepository.getProductsByNameContainsOrderByPriceDesc(productName);
        }
    }
}
