package com.serhiihurin.shop.online_shop.scheduler;

import com.serhiihurin.shop.online_shop.facades.interfaces.ProductFacade;
import com.serhiihurin.shop.online_shop.facades.interfaces.WishlistFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private final WishlistFacade wishlistFacade;
    private final ProductFacade productFacade;

    @Scheduled(cron = "0 0 9 * * *")
    public void checkForSales() {
        wishlistFacade.notifyUsersAboutSales();
    }

//    @Scheduled(cron = "0/10 * * * * *")
//    public void sendNotificationAboutProductAvailability() {
//
//    }
}
