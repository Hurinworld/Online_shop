package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.*;
import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.entity.*;
import com.serhiihurin.shop.online_shop.enums.Role;
import com.serhiihurin.shop.online_shop.enums.SortingType;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ShopRepository shopRepository;
    private final DiscountRepository discountRepository;
    private final EventRepository eventRepository;
    private final FeedbackRepository feedbackRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByShopId(Long id) {
        //TODO double request to repo //done
        return shopRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Could not find shop with ID: " + id))
                .getProducts();
    }

    @Override
    public List<ProductResponseDTO> searchProducts(List<Product> productList,
            String productName, SortingType sortingType, Double minimalPrice, Double maximalPrice
    ) {
        //todo refactor to switch-case style //done
        //TODO add sorting by rating of feedbacks(asc, desc) //done
        if(sortingType != null) {
            //TODO check is it correct
            return sortProducts(
                    filterProducts(productList, productName, minimalPrice, maximalPrice),
                    sortingType
            );
        } else {
            return modelMapper.map(
                    filterProducts(productList, productName, minimalPrice, maximalPrice),
                    new TypeToken<List<ProductResponseDTO>>(){
                    }.getType()
            );
        }
}

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Could not find product with ID: " + id));
    }

    @Override
    public Product addProduct(ProductRequestDTO productRequestDTO) {
        return productRepository.save(
                Product.builder()
                        .name(productRequestDTO.getName())
                        .description(productRequestDTO.getDescription())
                        .price(productRequestDTO.getPrice())
                        .amount(productRequestDTO.getAmount())
                        .shop(
                                shopRepository.findById(productRequestDTO.getShopId())
                                        .orElseThrow(() -> new ApiRequestException(
                                                "Could not find shop with ID: " + productRequestDTO.getShopId()
                                        ))
                        )
                        .build()
        );
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

    private List<Product> filterProducts(
            List<Product> productList,
            String productName,
            Double minimalPrice,
            Double maximalPrice
    ) {
        if (minimalPrice != null && maximalPrice != null) {
            return productList
                    .stream()
                    .filter(
                            product -> product.getPrice() > minimalPrice
                                    && product.getPrice() < maximalPrice
                                    && product.getName().contains(productName)
                    )
                    .toList();
        } else if(minimalPrice != null) {
            return productList
                    .stream()
                    .filter(product -> product.getPrice() > minimalPrice && product.getName().contains(productName))
                    .toList();
        } else if(maximalPrice != null) {
            return productList
                    .stream()
                    .filter(product -> product.getPrice() < maximalPrice && product.getName().contains(productName))
                    .toList();
        } else {
            return productList
                    .stream()
                    .filter(product -> product.getName().contains(productName))
                    .toList();
        }
    }

    private List<ProductResponseDTO> sortProducts(List<Product> productList, SortingType sortingType) {
        List<ProductResponseDTO> productResponseDTOS = modelMapper.map(
                productList,
                new TypeToken<List<ProductResponseDTO>>(){
                }.getType()
        );

        switch (sortingType) {
            case PRICE_ASCENDING -> productResponseDTOS.sort(Comparator.comparing(ProductResponseDTO::getPrice));
            case PRICE_DESCENDING -> productResponseDTOS.sort(Comparator.comparing(ProductResponseDTO::getPrice)
                    .reversed());
            case RATE_ASCENDING ->
                calculateProductsRate(productResponseDTOS).sort(Comparator.comparing(ProductResponseDTO::getRate));
            case RATE_DESCENDING ->
                    calculateProductsRate(productResponseDTOS).sort(Comparator.comparing(ProductResponseDTO::getRate)
                        .reversed());
            default -> throw new ApiRequestException("Wrong sorting type parameter value");
        }

        return productResponseDTOS;
    }

    private List<ProductResponseDTO> calculateProductsRate(List<ProductResponseDTO> productResponseDTOS) {
        for (ProductResponseDTO productResponseDTO : productResponseDTOS) {
            double productRate = 0.0;
            List<Feedback> feedbacks = feedbackRepository.getFeedbacksByProductId(productResponseDTO.getId());
            for (Feedback feedback : feedbacks) {
                productRate += feedback.getRate().getRateValue();
            }
            productResponseDTO.setRate(productRate/feedbacks.size());
        }

        return productResponseDTOS;
    }
}
