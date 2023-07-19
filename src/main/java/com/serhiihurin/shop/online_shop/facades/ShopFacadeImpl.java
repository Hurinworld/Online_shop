package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.ShopRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Shop;
import com.serhiihurin.shop.online_shop.services.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShopFacadeImpl implements ShopFacade{
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
        return shopService.saveShop(shopRequestDTO);
    }


    @Override
    public Shop updateShop(ShopRequestDTO shopRequestDTO) {
        Shop oldShop = shopService.getShop(shopRequestDTO.getId());

        Shop newShop = new Shop();
        newShop.setName(shopRequestDTO.getName() != null ? shopRequestDTO.getName() : oldShop.getName());
        newShop.setIncome(shopRequestDTO.getIncome() != null ? shopRequestDTO.getIncome() : oldShop.getIncome());

        return newShop;
    }

    @Override
    public void deleteShop(Long id) {
        shopService.deleteShop(id);
    }


}
