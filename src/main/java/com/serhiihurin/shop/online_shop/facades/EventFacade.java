package com.serhiihurin.shop.online_shop.facades;

import com.serhiihurin.shop.online_shop.dto.EventRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Event;
import com.serhiihurin.shop.online_shop.entity.User;

import java.util.List;

public interface EventFacade {
    List<Event> getEventsByEventCreatorId(Long eventsCreatorId);

    Event createEvent(User currentAuthenticatedUser, EventRequestDTO eventRequestDTO);

    void deleteEvent(User currentAuthenticatedUser, Long eventId);
}
