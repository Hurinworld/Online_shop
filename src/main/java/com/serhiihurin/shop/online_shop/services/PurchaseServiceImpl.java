package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.PurchaseRepository;
import com.serhiihurin.shop.online_shop.entity.Purchase;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.services.interfaces.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;

    @Override
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    @Override
    public List<Purchase> getPurchasesByClientId(Long id) {
        return purchaseRepository.getPurchasesByUserId(id);
    }

    @Override
    public Purchase savePurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    @Override
    public Purchase getPurchase(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Could not find purchase with id" + id));
    }

    @Override
    public void deletePurchase(Long id) {
        purchaseRepository.deleteById(id);
    }
}
