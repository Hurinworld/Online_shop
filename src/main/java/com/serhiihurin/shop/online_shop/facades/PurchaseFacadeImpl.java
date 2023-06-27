package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.entity.*;
import com.serhiihurin.shop.online_shop.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PurchaseFacadeImpl implements PurchaseFacade{
    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDataService productDataService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private PurchaseService purchaseService;

    @Override
    public Purchase makePurchase(Long clientId, List<Long> productIds, Purchase purchase) {
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
            shopService.saveShop(shop);
        }
        return purchase;
    }
}
