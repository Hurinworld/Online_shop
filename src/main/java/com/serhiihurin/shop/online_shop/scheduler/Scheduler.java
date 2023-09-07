package com.serhiihurin.shop.online_shop.scheduler;

import com.serhiihurin.shop.online_shop.facades.WishlistFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
//TODO rename it //done
public class Scheduler {
    private final WishlistFacade wishlistFacade;

    @Scheduled(cron = "0 0 9 * * *")
    public void checkForSales() {
        wishlistFacade.notifyUsersAboutSales();
    }
}
