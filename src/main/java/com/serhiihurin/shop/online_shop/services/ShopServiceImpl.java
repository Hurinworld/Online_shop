package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ShopRepository;
import com.serhiihurin.shop.online_shop.entity.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Override
    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    @Override
    public void saveShop(Shop shop) {
        shopRepository.save(shop);
    }

    @Override
    public Shop getShop(Long id) {
        Shop shop = null;
        Optional<Shop> optionalShop = shopRepository.findById(id);
        if (optionalShop.isPresent()) {
            shop = optionalShop.get();
        }
        return shop;
    }

    @Override
    public void deleteShop(Long id) {
        shopRepository.deleteById(id);
    }
}
