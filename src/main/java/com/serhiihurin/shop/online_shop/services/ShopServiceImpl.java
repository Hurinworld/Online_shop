package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.ShopRepository;
import com.serhiihurin.shop.online_shop.dto.ShopRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.services.interfaces.ShopService;
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
                .orElseThrow(() -> new ApiRequestException("Could not find shop with ID" + id));
    }

    @Override
    public Shop getShopByOwnerId(Long ownerId) {
        return shopRepository.getShopByOwnerId(ownerId)
                .orElseThrow(() -> new ApiRequestException("Could not find shop with owner ID" + ownerId));
    }


    @Override
    public Shop updateShop(ShopRequestDTO shopRequestDTO, Shop shop) {
        if (shopRequestDTO.getName() != null) {
            shop.setName(shopRequestDTO.getName());
        }
        if (shopRequestDTO.getIncome() != null) {
            shop.setIncome(shopRequestDTO.getIncome());
        }
        return shopRepository.save(shop);
    }

    @Override
    public Shop createShop(User currentAuthenticatedUser, ShopRequestDTO shopRequestDTO) {
        Shop shop = Shop.builder()
                .name(shopRequestDTO.getName())
                .owner(currentAuthenticatedUser)
                .build();
        shop.setIncome(shopRequestDTO.getIncome() != null ? shopRequestDTO.getIncome() : 0.0);
        return shopRepository.save(shop);
    }

    @Override
    public void deleteShop(Long id) {
        shopRepository.deleteById(id);
    }
}
