package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.EventRequestDTO;
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
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventFacadeImpl implements EventFacade{
    private final EventService eventService;
    private final EmailService emailService;
    private final UserService userService;
    private final ProductService productService;
    private final NotificationService notificationService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public List<Event> getEventsByEventCreatorId(Long eventsCreatorId) {
        return eventService.getEventsByEventCreatorId(eventsCreatorId);
    }

    @Override
    public Event createEvent(User currentAuthenticatedUser, EventRequestDTO eventRequestDTO) {
        Event event = eventService.createEvent(currentAuthenticatedUser, eventRequestDTO);
        Map<String, Integer> productsForSale = eventRequestDTO.getProductsForSale();
        for (String name : productsForSale.keySet()) {
            productService.putProductOnSale(currentAuthenticatedUser, name, productsForSale.get(name), event);
        }
        List<User> userList = userService.getAllUsers();
        userList.forEach(user -> {
            emailService.sendNotificationAboutEventStart(user.getEmail(), event);
            notificationService.addNotification(
                    Notification.builder()
                            .title("Notification about " + event.getTitle() + " event start")
                            .text("Hi " + currentAuthenticatedUser.getFirstName() + "! \n\n"
                                    + "Visit Online Shop and checkout new discounts!\n"
                                    + " Discount will be valid from " + event.getStartDateTime()
                                    + " to " + event.getEndDateTime()
                                    + "\n\nDon't miss the opportunity to get products for nice prices!")
                            .sendDateTime(
                                    LocalDateTime.parse(
                                            LocalDateTime.now().atZone(ZoneId.of("Z")).format(formatter)
                                    )
                            )
                            .build()
            );
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
