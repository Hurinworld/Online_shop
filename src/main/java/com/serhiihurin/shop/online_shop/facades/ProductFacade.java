package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductFacade {
    List<ProductResponseDTO> getAllProducts();

    List<ProductResponseDTO> getAllProductsByShopId(Long id);

    List<ProductResponseDTO> searchProductsGlobally(
            String productName,
            String sortingParameterValue,
            String sortingTypeValue,
            Double minimalPrice,
            Double maximalPrice
    );

    List<ProductResponseDTO> searchProductsInShop(
            User currentAuthenticatedUser,
            String productName,
            String sortingParameterValue,
            String sortingTypeValue,
            Double minimalPrice,
            Double maximalPrice
    );

    ProductResponseDTO getProduct(Long id);

    ProductResponseDTO addProduct(
            User currentAuthenticatedUser,
            ProductRequestDTO productRequestDTO,
            MultipartFile[] files
    );

    void putProductOnSale(User currentAuthenticatedUser, Long productId, int discountPercent);

    Product updateProduct(User currentAuthenticatedUser, Long id, ProductRequestDTO productRequestDTO);

    void removeProductFromSale(User currentAuthenticatedUser, Long productId);

    void deleteProduct(User currentAuthenticatedUser, Long id);
}
