package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ShopRequestDTO;
import com.serhiihurin.shop.online_shop.entity.*;
import com.serhiihurin.shop.online_shop.services.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PurchaseFacadeImpl implements PurchaseFacade{
    private final ClientService clientService;

    private final ProductService productService;

    private final ProductDataService productDataService;

    private final ShopService shopService;

    private final PurchaseService purchaseService;

    private final ModelMapper modelMapper;


    @Override
    public List<Purchase> getAllPurchases() {
        return purchaseService.getAllPurchases();
    }

    @Override
    public List<Purchase> getPurchasesByClientId(Long clientId) {
        return purchaseService.getPurchasesByClientId(clientId);
    }

    @Override
    public Purchase getPurchase(Long id) {
        return purchaseService.getPurchase(id);
    }

    @Override
    @Transactional
    public Purchase makePurchase(Long clientId, List<Long> productIds) {
        Purchase purchase = new Purchase();
        Client client = clientService.getClient(clientId);
        for(Long productId : productIds) {
            Product product = productService.getProduct(productId);
            ProductData productData = product.getProductData();
            Shop shop = productData.getShop();

            product.setBought(true);
            productData.setCount(productData.getCount() - 1);
            client.setCash(client.getCash() - productData.getPrice());
            shop.setIncome(shop.getIncome() + productData.getPrice());
            purchase.setClient(client);
            purchase.setTime(LocalDateTime.now());

            purchaseService.savePurchase(purchase);
            product.setPurchase(purchase);
            productService.saveProduct(product);
            clientService.saveClient(client);
            productDataService.saveProductData(productData);
            shopService.saveShop(modelMapper.map(shop, ShopRequestDTO.class));
        }
        return purchase;
    }

    @Override
    public void deletePurchase(Long id) {
        purchaseService.deletePurchase(id);
    }
}
