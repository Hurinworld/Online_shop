package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.EventRepository;
import com.serhiihurin.shop.online_shop.dto.EventRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Event;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.exception.UnauthorizedAccessException;
import com.serhiihurin.shop.online_shop.services.interfaces.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public List<Event> getEventsByEventCreatorId(Long eventsCreatorId) {
        return eventRepository.getEventsByEventCreatorId(eventsCreatorId)
                .orElseThrow(() -> new ApiRequestException("Wrong user ID or events doesn't exist"));
    }

    @Override
    public Event createEvent(User currentAuthenticatedUser, EventRequestDTO eventRequestDTO) {
        return eventRepository.save(
                Event.builder()
                        .title(eventRequestDTO.getTitle())
                        .description(eventRequestDTO.getDescription())
                        .startDateTime(LocalDateTime.parse(
                                LocalDateTime.now().format(formatter), formatter
                        ))
                        .endDateTime(LocalDateTime.parse(
                                eventRequestDTO.getEndDateTime().format(formatter), formatter
                        ))
                        .eventCreator(currentAuthenticatedUser)
                        .build()
        );
    }

    @Override
    public void deleteEvent(User currentAuthenticatedUser, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ApiRequestException("Could not find event with ID: " + eventId));

        if (!event.getEventCreator().getId().equals(currentAuthenticatedUser.getId())) {
            throw new UnauthorizedAccessException("Access denied. Wrong event ID");
        }

        eventRepository.delete(event);
    }
}
