package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.FeedbackRepository;
import com.serhiihurin.shop.online_shop.dao.ProductRepository;
import com.serhiihurin.shop.online_shop.dao.ShopRepository;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.enums.SortingParameter;
import com.serhiihurin.shop.online_shop.enums.SortingType;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService{
    private final ProductRepository productRepository;
    private final FeedbackRepository feedbackRepository;
    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<ProductResponseDTO> searchProductsGlobally(
            String productName,
            String sortingParameterValue,
            String sortingTypeValue,
            Double minimalPrice,
            Double maximalPrice
    ) {
        SortingParameter sortingParameter = null;
        if (sortingParameterValue != null) {
            sortingParameter = SortingParameter.valueOf(sortingParameterValue.toUpperCase());
        }

        SortingType sortingType = null;
        if (sortingTypeValue != null) {
            sortingType = SortingType.valueOf(sortingTypeValue.toUpperCase());
        }

        if (sortingParameter != null && sortingType != null) {
            return sortProducts(
                    filterProductsGlobally(productName, minimalPrice, maximalPrice),
                    sortingParameter,
                    sortingType
            );
            //TODO add default sort by + default sortDirection(DESC for example)
        } else {
            return modelMapper.map(
                    filterProductsGlobally(productName, minimalPrice, maximalPrice),
                    new TypeToken<List<ProductResponseDTO>>(){
                    }.getType()
            );
        }
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
        Long shopId = shopRepository.getShopByOwnerId(currentAuthenticatedUser.getId()).orElseThrow(
                () -> new ApiRequestException("Could not find shop with owner ID: " + currentAuthenticatedUser.getId())
        ).getId();

        SortingParameter sortingParameter = null;
        if (sortingParameterValue != null) {
            sortingParameter = SortingParameter.valueOf(sortingParameterValue.toUpperCase());
        }

        SortingType sortingType = null;
        if (sortingTypeValue != null) {
            sortingType = SortingType.valueOf(sortingTypeValue.toUpperCase());
        }

        if (sortingParameter != null && sortingType != null) {
            return sortProducts(
                    filterProductsInShop(shopId, productName, minimalPrice, maximalPrice),
                    sortingParameter,
                    sortingType
            );
        } else {
            return modelMapper.map(
                    filterProductsInShop(shopId, productName, minimalPrice, maximalPrice),
                    new TypeToken<List<ProductResponseDTO>>(){
                    }.getType()
            );
        }
    }

    private List<Product> filterProductsGlobally(
            String productName,
            Double minimalPrice,
            Double maximalPrice
    ) {
        if (productName != null && minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceBetween(productName, minimalPrice, maximalPrice);
        } else if (productName != null && minimalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceGreaterThan(productName, minimalPrice);
        } else if (productName != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceLessThan(productName, maximalPrice);
        } else if (productName != null) {
            return productRepository.getProductsByNameContains(productName);
        } else if (minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByPriceBetween(minimalPrice, maximalPrice);
        } else if(minimalPrice != null) {
            return productRepository.getProductsByPriceGreaterThan(minimalPrice);
        } else if(maximalPrice != null) {
            return productRepository.getProductsByPriceLessThan(maximalPrice);
        } else {
            return productRepository.findAll();
        }
    }

    private List<Product> filterProductsInShop(
            Long shopId,
            String productName,
            Double minimalPrice,
            Double maximalPrice
    ) {
        if (productName != null && minimalPrice != null && maximalPrice != null) {
            return productRepository
                    .getProductsByNameContainsAndShopIdAndPriceBetween(productName, shopId, minimalPrice, maximalPrice);
        } else if (productName != null && minimalPrice != null) {
            return productRepository
                    .getProductsByNameContainsAndShopIdAndPriceGreaterThan(productName, shopId, minimalPrice);
        } else if (productName != null && maximalPrice != null) {
            return productRepository
                    .getProductsByNameContainsAndShopIdAndPriceLessThan(productName, shopId, maximalPrice);
        } else if (productName != null) {
            return productRepository.getProductsByNameContainsAndShopId(productName, shopId);
        } else if (minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceBetween(shopId, minimalPrice, maximalPrice);
        } else if(minimalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceGreaterThan(shopId, minimalPrice);
        } else if(maximalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceLessThan(shopId, maximalPrice);
        } else {
            return productRepository.getProductsByShopId(shopId);
        }
    }

    private List<ProductResponseDTO> sortProducts(
            List<Product> productList,
            SortingParameter sortingParameter,
            SortingType sortingType
    ) {
        List<ProductResponseDTO> productResponseDTOS = modelMapper.map(
                productList,
                new TypeToken<List<ProductResponseDTO>>(){
                }.getType()
        );

        switch (sortingParameter) {
            case PRICE -> {
                switch (sortingType) {
                    case ASCENDING -> productResponseDTOS.sort(Comparator.comparing(ProductResponseDTO::getPrice));
                    case DESCENDING -> productResponseDTOS.sort(Comparator.comparing(ProductResponseDTO::getPrice)
                            .reversed());
                }
            }
            case RATE -> {
                switch (sortingType) {
                    case ASCENDING -> calculateProductsRate(productResponseDTOS)
                            .sort(Comparator.comparing(ProductResponseDTO::getRate));
                    case DESCENDING -> calculateProductsRate(productResponseDTOS)
                            .sort(Comparator.comparing(ProductResponseDTO::getRate)
                                    .reversed());
                }
            }
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
