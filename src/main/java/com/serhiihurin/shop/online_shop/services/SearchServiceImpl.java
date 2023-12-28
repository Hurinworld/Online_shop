package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ProductRepository;
import com.serhiihurin.shop.online_shop.dao.ShopRepository;
import com.serhiihurin.shop.online_shop.dto.SearchRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.enums.SortingParameter;
import com.serhiihurin.shop.online_shop.enums.SortingDirection;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.services.interfaces.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;


    @Override
    public List<Product> searchProductsGlobally(SearchRequestDTO searchRequestDTO) {
        SortingParameter sortingParameter = validateSortingParameter(searchRequestDTO);

        SortingDirection sortingDirection = validateSortingDirection(searchRequestDTO);

        return filterAndSortProductsGlobally(
                searchRequestDTO.getProductName(),
                searchRequestDTO.getMinimalPrice(),

                searchRequestDTO.getMaximalPrice(),
                sortingParameter,
                sortingDirection
        );

    }

    @Override
    public List<Product> searchProductsInShop(SearchRequestDTO searchRequestDTO) {
        Long shopId = shopRepository.getShopByOwnerId(searchRequestDTO.getCurrentAuthenticatedUser().getId())
                .orElseThrow(
                        () -> new ApiRequestException(
                                "Could not find shop with owner ID: " +
                                        searchRequestDTO.getCurrentAuthenticatedUser().getId())
                ).getId();

        SortingParameter sortingParameter = validateSortingParameter(searchRequestDTO);

        SortingDirection sortingDirection = validateSortingDirection(searchRequestDTO);

        return filterAndSortProductsInShop(
                shopId,
                searchRequestDTO.getProductName(),
                searchRequestDTO.getMinimalPrice(),
                searchRequestDTO.getMaximalPrice(),
                sortingParameter,
                sortingDirection
        );
    }

    private List<Product> filterAndSortProductsGlobally(
            String productName,
            Double minimalPrice,
            Double maximalPrice,
            SortingParameter sortingParameter,
            SortingDirection sortingDirection
    ) {
        List<Product> products;

        switch (sortingParameter) {
            case PRICE -> products = sortByPrice(productName, minimalPrice, maximalPrice, sortingDirection);
            case RATE -> products = sortByRating(productName, minimalPrice, maximalPrice, sortingDirection);
            default -> products = filterProducts(productName, minimalPrice, maximalPrice);
        }

        return products;
    }

    private List<Product> sortByPrice(
            String productName,
            Double minimalPrice,
            Double maximalPrice,
            SortingDirection sortingDirection
    ) {
        //TODO refactor it //done
        return switch (sortingDirection) {
            case DESCENDING -> getProductsSortedByPriceDesc(productName, minimalPrice, maximalPrice);
            case ASCENDING -> getProductsSortedByPriceAsc(productName, minimalPrice, maximalPrice);
        };

    }

    private List<Product> sortByRating(
            String productName,
            Double minimalPrice,
            Double maximalPrice,
            SortingDirection sortingDirection
    ) {
        //TODO refactor it //done
        return switch (sortingDirection) {
            case DESCENDING -> getProductsSortedByRatingDesc(productName, minimalPrice, maximalPrice);
            case ASCENDING -> getProductsSortedByRatingAsc(productName, minimalPrice, maximalPrice);
        };

    }

    private List<Product> filterProducts(String productName, Double minimalPrice, Double maximalPrice) {
        if (productName != null && minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceBetween(productName, minimalPrice, maximalPrice);
        } else if (productName != null && minimalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceLessThan(productName, minimalPrice);
        } else if (productName != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceGreaterThan(productName, maximalPrice);
        } else if (productName != null) {
            return productRepository.getProductsByNameContains(productName);
        } else if (minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByPriceBetween(minimalPrice, maximalPrice);
        } else if (minimalPrice != null) {
            return productRepository.getProductsByPriceGreaterThan(minimalPrice);
        } else if (maximalPrice != null) {
            return productRepository.getProductsByPriceLessThan(maximalPrice);
        } else {
            return productRepository.findAll();
        }
    }

    private List<Product> getProductsSortedByPriceDesc(String productName, Double minimalPrice, Double maximalPrice) {
        if (productName != null && minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceBetweenOrderByPriceDesc(
                    productName,
                    minimalPrice,
                    maximalPrice
            );
        } else if (productName != null && minimalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceGreaterThanOrderByPriceDesc(
                    productName,
                    minimalPrice
            );
        } else if (productName != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceLessThanOrderByPriceDesc(
                    productName,
                    maximalPrice
            );
        } else if (productName != null) {
            return productRepository.getProductsByNameContainsOrderByPriceDesc(productName);
        } else if (minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByPriceBetweenOrderByPriceDesc(minimalPrice, maximalPrice);
        } else if (minimalPrice != null) {
            return productRepository.getProductsByPriceGreaterThanOrderByPriceDesc(minimalPrice);
        } else if (maximalPrice != null) {
            return productRepository.getProductsByPriceLessThanOrderByPriceDesc(maximalPrice);
        } else {
            return productRepository.findAllOrderByPriceDesc();
        }
    }

    private List<Product> getProductsSortedByPriceAsc(String productName, Double minimalPrice, Double maximalPrice) {
        if (productName != null && minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceBetweenOrderByPriceAsc(
                    productName,
                    minimalPrice,
                    maximalPrice
            );
        } else if (productName != null && minimalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceGreaterThanOrderByPriceAsc(
                    productName,
                    minimalPrice
            );
        } else if (productName != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceLessThanOrderByPriceAsc(
                    productName,
                    maximalPrice
            );
        } else if (productName != null) {
            return productRepository.getProductsByNameContainsOrderByPriceAsc(productName);
        } else if (minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByPriceBetweenOrderByPriceAsc(minimalPrice, maximalPrice);
        } else if (minimalPrice != null) {
            return productRepository.getProductsByPriceGreaterThanOrderByPriceAsc(minimalPrice);
        } else if (maximalPrice != null) {
            return productRepository.getProductsByPriceLessThanOrderByPriceAsc(maximalPrice);
        } else {
            return productRepository.findAllOrderByPriceAsc();
        }
    }

    private List<Product> getProductsSortedByRatingDesc(String productName, Double minimalPrice, Double maximalPrice) {
        if (productName != null && minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceBetweenOrderByAverageRatingDesc(
                    productName,
                    minimalPrice,
                    maximalPrice
            );
        } else if (productName != null && minimalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceGreaterThanOrderByAverageRatingDesc(
                    productName,
                    minimalPrice
            );
        } else if (productName != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceLessThanOrderByAverageRatingDesc(
                    productName,
                    maximalPrice
            );
        } else if (productName != null) {
            return productRepository.getProductsByNameContainsOrderByAverageRatingDesc(productName);
        } else if (minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByPriceBetweenOrderByAverageRatingDesc(minimalPrice, maximalPrice);
        } else if (minimalPrice != null) {
            return productRepository.getProductsByPriceGreaterThanOrderByAverageRatingDesc(minimalPrice);
        } else if (maximalPrice != null) {
            return productRepository.getProductsByPriceLessThanOrderByAverageRatingDesc(maximalPrice);
        } else {
            return productRepository.findAllOrderByAverageRatingDesc();
        }
    }

    private List<Product> getProductsSortedByRatingAsc(String productName, Double minimalPrice, Double maximalPrice) {
        if (productName != null && minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceBetweenOrderByAverageRatingAsc(
                    productName,
                    minimalPrice,
                    maximalPrice
            );
        } else if (productName != null && minimalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceGreaterThanOrderByAverageRatingAsc(
                    productName,
                    minimalPrice
            );
        } else if (productName != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndPriceLessThanOrderByAverageRatingAsc(
                    productName,
                    maximalPrice
            );
        } else if (productName != null) {
            return productRepository.getProductsByNameContainsOrderByAverageRatingAsc(productName);
        } else if (minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByPriceBetweenOrderByAverageRatingAsc(minimalPrice, maximalPrice);
        } else if (minimalPrice != null) {
            return productRepository.getProductsByPriceGreaterThanOrderByAverageRatingAsc(minimalPrice);
        } else if (maximalPrice != null) {
            return productRepository.getProductsByPriceLessThanOrderByAverageRatingAsc(maximalPrice);
        } else {
            return productRepository.findAllOrderByAverageRatingAsc();
        }
    }


    private List<Product> filterAndSortProductsInShop(
            Long shopId,
            String productName,
            Double minimalPrice,
            Double maximalPrice,
            SortingParameter sortingParameter,
            SortingDirection sortingDirection
    ) {
        List<Product> products;

        switch (sortingParameter) {
            case PRICE -> products =
                    sortByPriceInShop(shopId, productName, minimalPrice, maximalPrice, sortingDirection);
            case RATE -> products =
                    sortByRatingInShop(shopId, productName, minimalPrice, maximalPrice, sortingDirection);
            default -> products = filterProductsInShop(shopId, productName, minimalPrice, maximalPrice);
        }

        return products;
    }

    private List<Product> sortByPriceInShop(
            Long shopId,
            String productName,
            Double minimalPrice,
            Double maximalPrice,
            SortingDirection sortingDirection
    ) {
        if (productName != null) {
            return switch (sortingDirection) {
                case DESCENDING -> getProductsInShopSortedByPriceDesc(productName, shopId, minimalPrice, maximalPrice);
                case ASCENDING -> getProductsInShopSortedByPriceAsc(productName, shopId, minimalPrice, maximalPrice);
            };
        } else {
            return switch (sortingDirection) {
                case DESCENDING -> getProductsInShopSortedByPriceDesc(null, shopId, minimalPrice, maximalPrice);
                case ASCENDING -> getProductsInShopSortedByPriceAsc(null, shopId, minimalPrice, maximalPrice);
            };
        }
    }

    private List<Product> sortByRatingInShop(
            Long shopId,
            String productName,
            Double minimalPrice,
            Double maximalPrice,
            SortingDirection sortingDirection
    ) {
        if (productName != null) {
            return switch (sortingDirection) {
                case DESCENDING -> getProductsInShopSortedByRatingDesc(productName, shopId, minimalPrice, maximalPrice);
                case ASCENDING -> getProductsInShopSortedByRatingAsc(productName, shopId, minimalPrice, maximalPrice);
            };
        } else {
            return switch (sortingDirection) {
                case DESCENDING -> getProductsInShopSortedByRatingDesc(null, shopId, minimalPrice, maximalPrice);
                case ASCENDING -> getProductsInShopSortedByRatingAsc(null, shopId, minimalPrice, maximalPrice);
            };
        }
    }

    private List<Product> filterProductsInShop(
            Long shopId,
            String productName,
            Double minimalPrice,
            Double maximalPrice
    ) {
        if (productName != null && minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndShopIdAndPriceBetween(
                    productName,
                    shopId,
                    minimalPrice,
                    maximalPrice
            );
        } else if (productName != null && minimalPrice != null) {
            return productRepository.getProductsByNameContainsAndShopIdAndPriceLessThan(
                    productName,
                    shopId,
                    minimalPrice
            );
        } else if (productName != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndShopIdAndPriceGreaterThan(
                    productName,
                    shopId,
                    maximalPrice
            );
        } else if (productName != null) {
            return productRepository.getProductsByNameContainsAndShopId(productName, shopId);
        } else if (minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceBetween(shopId, minimalPrice, maximalPrice);
        } else if (minimalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceGreaterThan(shopId, minimalPrice);
        } else if (maximalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceLessThan(shopId, maximalPrice);
        } else {
            return productRepository.getProductsByShopId(shopId);
        }
    }

    private List<Product> getProductsInShopSortedByPriceDesc(
            String productName,
            Long shopId,
            Double minimalPrice,
            Double maximalPrice
    ) {
        if (productName != null && minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndShopIdAndPriceBetweenOrderByPriceDesc(
                    productName,
                    shopId,
                    minimalPrice,
                    maximalPrice
            );
        } else if (productName != null && minimalPrice != null) {
            return productRepository.getProductsByNameContainsAndShopIdAndPriceGreaterThanOrderByPriceDesc(
                    productName,
                    shopId,
                    minimalPrice
            );
        } else if (productName != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndShopIdAndPriceLessThanOrderByPriceDesc(
                    productName,
                    shopId,
                    maximalPrice
            );
        } else if (productName != null) {
            return productRepository.getProductsByNameContainsAndShopIdOrderByPriceDesc(productName, shopId);
        } else if (minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceBetweenOrderByPriceDesc(
                    shopId,
                    minimalPrice,
                    maximalPrice
            );
        } else if (minimalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceGreaterThanOrderByPriceDesc(shopId, minimalPrice);
        } else if (maximalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceLessThanOrderByPriceDesc(shopId, maximalPrice);
        } else {
            return productRepository.getProductsByShopIdOrderByPriceDesc(shopId);
        }
    }

    private List<Product> getProductsInShopSortedByPriceAsc(
            String productName,
            Long shopId,
            Double minimalPrice,
            Double maximalPrice
    ) {
        if (productName != null && minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndShopIdAndPriceBetweenOrderByPriceAsc(
                    productName,
                    shopId,
                    minimalPrice,
                    maximalPrice
            );
        } else if (productName != null && minimalPrice != null) {
            return productRepository.getProductsByNameContainsAndShopIdAndPriceGreaterThanOrderByPriceAsc(
                    productName,
                    shopId,
                    minimalPrice
            );
        } else if (productName != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndShopIdAndPriceLessThanOrderByPriceAsc(
                    productName,
                    shopId,
                    maximalPrice
            );
        } else if (productName != null) {
            return productRepository.getProductsByNameContainsAndShopIdOrderByPriceAsc(productName, shopId);
        } else if (minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceBetweenOrderByPriceAsc(
                    shopId,
                    minimalPrice,
                    maximalPrice
            );
        } else if (minimalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceGreaterThanOrderByPriceAsc(shopId, minimalPrice);
        } else if (maximalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceLessThanOrderByPriceAsc(shopId, maximalPrice);
        } else {
            return productRepository.getProductsByShopIdOrderByPriceAsc(shopId);
        }
    }

    private List<Product> getProductsInShopSortedByRatingDesc(
            String productName,
            Long shopId,
            Double minimalPrice,
            Double maximalPrice
    ) {
        if (productName != null && minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndShopIdAndPriceBetweenOrderByAverageRatingDesc(
                    productName,
                    shopId,
                    minimalPrice,
                    maximalPrice
            );
        } else if (productName != null && minimalPrice != null) {
            return productRepository.getProductsByNameContainsAndShopIdAndPriceGreaterThanOrderByAverageRatingDesc(
                    productName,
                    shopId,
                    minimalPrice
            );
        } else if (productName != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndShopIdAndPriceLessThanOrderByAverageRatingDesc(
                    productName,
                    shopId,
                    maximalPrice
            );
        } else if (productName != null) {
            return productRepository.getProductsByNameContainsAndShopIdOrderByAverageRatingDesc(productName, shopId);
        } else if (minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceBetweenOrderByAverageRatingDesc(
                    shopId,
                    minimalPrice,
                    maximalPrice
            );
        } else if (minimalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceGreaterThanOrderByAverageRatingDesc(shopId,
                    minimalPrice);
        } else if (maximalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceLessThanOrderByAverageRatingDesc(shopId, maximalPrice);
        } else {
            return productRepository.getProductsByShopIdOrderByAverageRatingDesc(shopId);
        }
    }

    private List<Product> getProductsInShopSortedByRatingAsc(
            String productName,
            Long shopId,
            Double minimalPrice,
            Double maximalPrice
    ) {
        if (productName != null && minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndShopIdAndPriceBetweenOrderByAverageRatingAsc(
                    productName,
                    shopId,
                    minimalPrice,
                    maximalPrice
            );
        } else if (productName != null && minimalPrice != null) {
            return productRepository.getProductsByNameContainsAndShopIdAndPriceGreaterThanOrderByAverageRatingAsc(
                    productName,
                    shopId,
                    minimalPrice
            );
        } else if (productName != null && maximalPrice != null) {
            return productRepository.getProductsByNameContainsAndShopIdAndPriceLessThanOrderByAverageRatingAsc(
                    productName,
                    shopId,
                    maximalPrice
            );
        } else if (productName != null) {
            return productRepository.getProductsByNameContainsAndShopIdOrderByAverageRatingAsc(productName, shopId);
        } else if (minimalPrice != null && maximalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceBetweenOrderByAverageRatingAsc(
                    shopId,
                    minimalPrice,
                    maximalPrice
            );
        } else if (minimalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceGreaterThanOrderByAverageRatingAsc(shopId,
                    minimalPrice);
        } else if (maximalPrice != null) {
            return productRepository.getProductsByShopIdAndPriceLessThanOrderByAverageRatingAsc(shopId, maximalPrice);
        } else {
            return productRepository.getProductsByShopIdOrderByAverageRatingAsc(shopId);
        }
    }


    private SortingParameter validateSortingParameter(SearchRequestDTO searchRequestDTO) {
        if (searchRequestDTO.getSortingParameterValue() != null) {
            return SortingParameter.valueOf(searchRequestDTO.getSortingParameterValue().toUpperCase());
        }
        return SortingParameter.RATE;
    }

    //TODO rename method //done
    private SortingDirection validateSortingDirection(SearchRequestDTO searchRequestDTO) {
        if (searchRequestDTO.getSortingDirection() != null) {
            return SortingDirection.valueOf(searchRequestDTO.getSortingDirection().toUpperCase());
        }
        return SortingDirection.DESCENDING;
    }
}
