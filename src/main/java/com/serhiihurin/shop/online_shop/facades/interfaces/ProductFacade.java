package com.serhiihurin.shop.online_shop.facades.interfaces;

import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductResponseDTO;
import com.serhiihurin.shop.online_shop.dto.SearchRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductFacade {
    List<ProductResponseDTO> getAllProducts();

    List<ProductResponseDTO> getAllProductsByShopId(Long id);

    List<ProductResponseDTO> searchProductsGlobally(SearchRequestDTO searchRequestDTO);

    List<ProductResponseDTO> searchProductsInShop(SearchRequestDTO searchRequestDTO);

    ProductResponseDTO getProduct(Long id);

    ProductResponseDTO addProduct(
            User currentAuthenticatedUser,
            ProductRequestDTO productRequestDTO
    );

    void putProductOnSale(User currentAuthenticatedUser, Long productId, int discountPercent);

    ProductResponseDTO updateProduct(User currentAuthenticatedUser, Long id, ProductRequestDTO productRequestDTO);

    void removeProductFromSale(User currentAuthenticatedUser, Long productId);

    void deleteProduct(User currentAuthenticatedUser, Long productId);

    void subscribeForNotificationAboutAvailability(Long productId, User currentAuthenticatedUser);
}
