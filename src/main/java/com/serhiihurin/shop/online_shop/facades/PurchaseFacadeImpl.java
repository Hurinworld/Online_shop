package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.PurchaseRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ShopRequestDTO;
import com.serhiihurin.shop.online_shop.entity.*;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.exception.PurchaseException;
import com.serhiihurin.shop.online_shop.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PurchaseFacadeImpl implements PurchaseFacade{
    private final UserService userService;

    private final ProductService productService;

    private final ShopService shopService;

    private final PurchaseService purchaseService;

    private final PurchaseDetailsService purchaseDetailsService;

    private final ModelMapper modelMapper;


    @Override
    public List<Purchase> getAllPurchases() {
        log.info("Admin: getting list of purchases");
        return purchaseService.getAllPurchases();
    }

    @Override
    public List<Purchase> getPurchasesByClientId(Long clientId) {
        if (userService.getUser(clientId) == null) {
            throw new ApiRequestException("Could not find client with id " + clientId);
        }
        return purchaseService.getPurchasesByClientId(clientId);
    }

    @Override
    public Purchase getPurchase(Long id) {
        return purchaseService.getPurchase(id);
    }

    @Override
    @Transactional
    public Purchase makePurchase(User currentAuthenticatedUser, PurchaseRequestDTO purchaseRequestDTO) {
        Purchase purchase = new Purchase();
        Map<Long, Integer> productsCount = purchaseRequestDTO.getProductsCount();

        for(Long productId : productsCount.keySet()) {
            Product product = productService.getProduct(productId);
            Double totalPrice = product.getPrice() * productsCount.get(productId);
            Shop shop = product.getShop();

            if (currentAuthenticatedUser.getCash() < totalPrice) {
                throw new PurchaseException("Purchase failed. Not enough money in wallet");
            }

            product.setAmount(product.getAmount() - productsCount.get(productId));
            currentAuthenticatedUser.setCash(currentAuthenticatedUser.getCash() - totalPrice);
            shop.setIncome(shop.getIncome() + totalPrice);
            purchase.setUser(currentAuthenticatedUser);
            purchase.setTime(LocalDateTime.now());

            purchase.getPurchaseDetails().add(
                    purchaseDetailsService.savePurchaseDetails(
                            PurchaseDetails.builder()
                                    .purchase(purchase)
                                    .product(product)
                                    .amount(productsCount.get(productId))
                                    .totalPrice(totalPrice)
                                    .build()
                    )
            );

            productService.addProduct(product);
            shopService.updateShop(modelMapper.map(shop, ShopRequestDTO.class), shop);
        }

        purchaseService.savePurchase(purchase);
        userService.saveUser(currentAuthenticatedUser);

        log.info("Successful purchase proceeding. Purchase id: {}", purchase.getId());
        return purchase;
    }

    @Override
    public void deletePurchase(Long id) {
        log.info("Deleting of purchase with id: {}", id);
        purchaseService.deletePurchase(id);
    }
}
