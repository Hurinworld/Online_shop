package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.PurchaseDetailsRepository;
import com.serhiihurin.shop.online_shop.entity.PurchaseDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseDetailsServiceImpl implements PurchaseDetailsService{
    private final PurchaseDetailsRepository purchaseDetailsRepository;

    @Override
    public PurchaseDetails savePurchaseDetails(PurchaseDetails purchaseDetails) {
        return purchaseDetailsRepository.save(purchaseDetails);
    }
}
