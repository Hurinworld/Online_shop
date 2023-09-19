package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.EventRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Event;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.services.EmailService;
import com.serhiihurin.shop.online_shop.services.EventService;
import com.serhiihurin.shop.online_shop.services.ProductService;
import com.serhiihurin.shop.online_shop.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
//        userList.add(userService.getUserByEmail("sergey.gurin2015@gmaiffffl.com"));
        userList.forEach(user -> emailService.sendNotificationAboutEventStart(user.getEmail(), event));
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
