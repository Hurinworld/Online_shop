package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.DiscountRepository;
import com.serhiihurin.shop.online_shop.dao.ProductRepository;
import com.serhiihurin.shop.online_shop.dao.ShopRepository;
import com.serhiihurin.shop.online_shop.dto.ProductRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Discount;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.enums.Role;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final DiscountRepository discountRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByShopId(Long id) {
        return productRepository.getProductsByShopId(id)
                .orElseThrow(() -> new ApiRequestException("Could not find products from shop with ID: " + id));
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

        if (productRequestDTO.getShopId() != null) {
            product.setShop(
                    shopRepository.findById(productRequestDTO.getShopId())
                            .orElseThrow(() -> new ApiRequestException("Could not find shop with ID: " +
                                    productRequestDTO.getShopId()))
            );
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
    public void increaseProductAmount(User currentAuthenticatedUser, Product product, Integer amount) {
        if (amount == null || amount < 0){
            throw new ApiRequestException("Wrong amount parameter");
        }
        if (!product.getShop().getOwner().getId().equals(currentAuthenticatedUser.getId())) {
            throw new UnauthorizedAccessException("Access denied. Wrong product ID");
        }
        product.setAmount(product.getAmount() + amount);
        productRepository.save(product);
    }

    @Override
    public <T> void putProductOnSale(User currentAuthenticatedUser, T productSearchValue, int discountPercent) {
        Product product;

        if (productSearchValue.getClass()==Long.class) {
            product = productRepository.findById((Long) productSearchValue)
                    .orElseThrow(() -> new ApiRequestException("Could not find product with ID: "
                            + productSearchValue));
        } else if (productSearchValue instanceof String) {
            product = productRepository.getProductByName((String) productSearchValue)
                    .orElseThrow(() -> new ApiRequestException("Could not find product with name: "
                            + productSearchValue));
        } else {
            throw new ApiRequestException("Wrong parameter value "
                    + productSearchValue + ". The value should be of type Long or String");
        }

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
                .discountPercent(discountPercent)
                .build()
        );
        productRepository.save(product);
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
        Map<Product, Integer> eventProducts = new HashMap<>();
        List<Discount> eventProductsDiscounts = discountRepository.findDiscountsByEventId(eventId)
                .orElseThrow(() -> new ApiRequestException("Could not find event with ID: " + eventId));

        eventProductsDiscounts.forEach(
                discount -> eventProducts.put(
                        productRepository
                                .findById(discount.getProduct().getId())
                                .orElseThrow(() -> new ApiRequestException("Could not find product with ID: "
                                        + discount.getProduct().getId())),
                        discount.getDiscountPercent()
                )
        );

        for (Product product : eventProducts.keySet()) {
            product.setOnSale(false);
            product.setPrice(product.getPrice() / (1.0 - (eventProducts.get(product) / 100.0)));
            productRepository.save(product);
        }

        discountRepository.deleteAll(eventProductsDiscounts);
    }

    @Override
    public void deleteProduct(User currentAuthenticatedUser, Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Could not find product with ID: " + id));
        if (!product.getShop().getOwner().getId().equals(currentAuthenticatedUser.getId())) {
            throw new UnauthorizedAccessException("Access denied.");
        }
        productRepository.deleteById(id);
    }
}
