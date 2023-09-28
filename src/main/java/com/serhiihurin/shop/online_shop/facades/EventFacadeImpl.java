package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.EventRequestDTO;
import com.serhiihurin.shop.online_shop.dto.ProductForSaleRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Event;
import com.serhiihurin.shop.online_shop.entity.Notification;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventFacadeImpl implements EventFacade{
    private final EventService eventService;
    private final EmailService emailService;
    private final UserService userService;
    private final ProductService productService;
    private final NotificationService notificationService;

    @Override
    public List<Event> getEventsByEventCreatorId(Long eventsCreatorId) {
        return eventService.getEventsByEventCreatorId(eventsCreatorId);
    }

    @Override
    public Event createEvent(User currentAuthenticatedUser, EventRequestDTO eventRequestDTO) {
        Event event = eventService.createEvent(currentAuthenticatedUser, eventRequestDTO);
        for (ProductForSaleRequestDTO product : eventRequestDTO.getProductsForSale()) {
            productService.putProductOnSale(
                    currentAuthenticatedUser, product.getProductId(),
                    product.getDiscountPercent(), event
            );
        }
        List<User> userList = userService.getAllUsers();
        userList.forEach(user -> {
            emailService.sendNotificationAboutEventStart(user.getEmail(), event);
            //TODO extract this logic to the notificationService //done
            notificationService.addNotification(event.getTitle(), event.getDescription());
        });
        log.info("Sent notification about {} event start at {}", eventRequestDTO.getTitle(), LocalDateTime.now());
        return event;
    }

    @Override
    public void deleteEvent(User currentAuthenticatedUser, Long eventId) {
        productService.removeEventProductsFromSale(eventId);
        eventService.deleteEvent(currentAuthenticatedUser, eventId);
        log.info("deleted event with ID: {}", eventId);
    }
}
