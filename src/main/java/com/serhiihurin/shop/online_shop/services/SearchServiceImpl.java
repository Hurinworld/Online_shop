package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.FeedbackRepository;
import com.serhiihurin.shop.online_shop.dao.ProductRepository;
import com.serhiihurin.shop.online_shop.dao.ShopRepository;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.dto.SearchRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.enums.SortingParameter;
import com.serhiihurin.shop.online_shop.enums.SortingDirection;
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
    public List<ProductResponseDTO> searchProductsGlobally(SearchRequestDTO searchRequestDTO) {
        SortingParameter sortingParameter = checkSortingParameter(searchRequestDTO);

        SortingDirection sortingDirection = checkSortingDirection(searchRequestDTO);

        if (sortingParameter != null && sortingDirection != null) {
            return sortProducts(
                    filterProductsGlobally(
                            searchRequestDTO.getProductName(),
                            searchRequestDTO.getMinimalPrice(),
                            searchRequestDTO.getMaximalPrice()
                    ),
                    sortingParameter,
                    sortingDirection
            );
            //TODO add default sort by + default sortDirection(DESC for example) //done
        } else {
            return sortProducts(
                    filterProductsGlobally(
                            searchRequestDTO.getProductName(),
                            searchRequestDTO.getMinimalPrice(),
                            searchRequestDTO.getMaximalPrice()
                    ),
                    SortingParameter.RATE,
                    SortingDirection.DESCENDING
            );
        }
    }

    @Override
    public List<ProductResponseDTO> searchProductsInShop(SearchRequestDTO searchRequestDTO) {
        Long shopId = shopRepository.getShopByOwnerId(searchRequestDTO.getCurrentAuthenticatedUser().getId())
                .orElseThrow(
                        () -> new ApiRequestException(
                                "Could not find shop with owner ID: " +
                                        searchRequestDTO.getCurrentAuthenticatedUser().getId())
        ).getId();

        SortingParameter sortingParameter = checkSortingParameter(searchRequestDTO);

        SortingDirection sortingDirection = checkSortingDirection(searchRequestDTO);

        if (sortingParameter != null && sortingDirection != null) {
            return sortProducts(
                    filterProductsInShop(
                            shopId,
                            searchRequestDTO.getProductName(),
                            searchRequestDTO.getMinimalPrice(),
                            searchRequestDTO.getMaximalPrice()
                    ),
                    sortingParameter,
                    sortingDirection
            );
        } else {
            return sortProducts(
                    filterProductsInShop(
                            shopId,
                            searchRequestDTO.getProductName(),
                            searchRequestDTO.getMinimalPrice(),
                            searchRequestDTO.getMaximalPrice()
                    ),
                    SortingParameter.RATE,
                    SortingDirection.DESCENDING
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
            SortingDirection sortingDirection
    ) {
        List<ProductResponseDTO> productResponseDTOS = modelMapper.map(
                productList,
                new TypeToken<List<ProductResponseDTO>>(){
                }.getType()
        );

        switch (sortingParameter) {
            case PRICE -> {
                switch (sortingDirection) {
                    case ASCENDING -> productResponseDTOS.sort(Comparator.comparing(ProductResponseDTO::getPrice));
                    case DESCENDING -> productResponseDTOS.sort(Comparator.comparing(ProductResponseDTO::getPrice)
                            .reversed());
                }
            }
            case RATE -> {
                switch (sortingDirection) {
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

    private SortingParameter checkSortingParameter(SearchRequestDTO searchRequestDTO) {
        if (searchRequestDTO.getSortingParameterValue() != null) {
            return SortingParameter.valueOf(searchRequestDTO.getSortingParameterValue().toUpperCase());
        } else return null;
    }

    private SortingDirection checkSortingDirection(SearchRequestDTO searchRequestDTO) {
        if (searchRequestDTO.getSortingDirection() != null) {
            return SortingDirection.valueOf(searchRequestDTO.getSortingDirection().toUpperCase());
        } else return null;
    }
}
