package com.serhiihurin.shop.online_shop.scheduler;

import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.facades.interfaces.WishlistFacade;
import com.serhiihurin.shop.online_shop.services.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {
    private final WishlistFacade wishlistFacade;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 9 * * *")
    public void checkForSales() {
        wishlistFacade.notifyUsersAboutSales();
    }


    @Scheduled(cron = "0/30 * * * * *")
    public void sendNotificationAboutProductAvailability() {
        Map<String, List<Product>> productAvailabilitySendingQueue = emailService.getProductAvailabilitySendingQueue();
        if (productAvailabilitySendingQueue.isEmpty()) {
            log.info("Nothing to send from email queue...");
        }
        for (String email : productAvailabilitySendingQueue.keySet()) {
            emailService.sendNotificationAboutProductAvailability(email, productAvailabilitySendingQueue.get(email));
        }
        productAvailabilitySendingQueue.clear();
        emailService.setProductAvailabilitySendingQueue(productAvailabilitySendingQueue);
    }
}
