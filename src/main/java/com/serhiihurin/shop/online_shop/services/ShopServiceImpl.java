package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ShopRepository;
import com.serhiihurin.shop.online_shop.dto.ShopRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final ShopRepository shopRepository;

    @Override
    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    @Override
    public Shop getShop(Long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Could not find shop with id" + id));
    }

    @Override
    public Shop updateShop(ShopRequestDTO shopRequestDTO, Shop shop) {
        if (shopRequestDTO.getName() != null) {
            shop.setName(shopRequestDTO.getName());
        }
        if (shopRequestDTO.getIncome() != null) {
            shop.setIncome(shopRequestDTO.getIncome());
        }
        return shop;
    }

    @Override
    public Shop saveShop(ShopRequestDTO shopRequestDTO) {
        Shop shop = Shop.builder()
                .name(shopRequestDTO.getName())
                .build();
        shop.setIncome(shopRequestDTO.getIncome() != null ? shopRequestDTO.getIncome() : 0.0);
        return shopRepository.save(shop);
    }

    @Override
    public void deleteShop(Long id) {
        shopRepository.deleteById(id);
    }
}
