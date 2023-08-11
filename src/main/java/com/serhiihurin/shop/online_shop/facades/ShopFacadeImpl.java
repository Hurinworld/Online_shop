package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ShopRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.services.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShopFacadeImpl implements ShopFacade {
    private final ShopService shopService;

    @Override
    public List<Shop> getAllShops() {
        return shopService.getAllShops();
    }

    @Override
    public Shop getShop(Long id) {
        return shopService.getShop(id);
    }

    @Override
    public Shop saveShop(ShopRequestDTO shopRequestDTO) {
        log.info("Adding new shop with name: {}", shopRequestDTO.getName());
        return shopService.saveShop(shopRequestDTO);
    }


    @Override
    public Shop updateShop(ShopRequestDTO shopRequestDTO) {
        Shop oldShop = shopService.getShop(shopRequestDTO.getId());
        log.info("Updating shop with id: {}", shopRequestDTO.getId());
        return shopService.updateShop(shopRequestDTO, oldShop);
    }

    @Override
    public void deleteShop(Long id) {
        log.info("Deleting shop with id: {}", id);
        shopService.deleteShop(id);
    }
}
