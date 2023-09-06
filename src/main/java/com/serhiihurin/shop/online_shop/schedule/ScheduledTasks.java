package com.serhiihurin.shop.online_shop.schedule;

import com.serhiihurin.shop.online_shop.facades.WishlistFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
//TODO rename it
public class ScheduledTasks {
    private final WishlistFacade wishlistFacade;

    @Scheduled(cron = "0 * * * * *")
    public void checkForSales() {
        wishlistFacade.notifyUsersAboutSales();
    }
}
