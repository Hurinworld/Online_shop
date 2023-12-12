package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.EventRepository;
import com.serhiihurin.shop.online_shop.dto.EventRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Event;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.enums.Role;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.exception.UnauthorizedAccessException;
import com.serhiihurin.shop.online_shop.services.interfaces.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {
    @Mock
    private EventRepository eventRepository;
    private EventService eventService;
    private Event testEvent;
    private User testUser;
    private ModelMapper modelMapper;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setUp() {
        eventService = new EventServiceImpl(eventRepository);
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        testUser = User.builder()
                .id(1L)
                .firstName("User")
                .lastName("For test")
                .email("testUser@gmail.com")
                .password("password")
                .cash(26665.0)
                .role(Role.SHOP_OWNER)
                .build();
        testEvent = Event.builder()
                .id(1L)
                .title("Test event")
                .description("Test event description")
                .eventCreator(testUser)
                .startDateTime(LocalDateTime.parse(
                                LocalDateTime.now().format(formatter), formatter
                        )
                )
                .endDateTime(
                        LocalDateTime.parse(
                                LocalDateTime.now().format(formatter), formatter
                        ).plusHours(1)
                )
                .build();
    }

    @Test
    void getEventsByEventCreatorId() {
        //given
        List<Event> expectedEvents = List.of(testEvent);

        Mockito.when(eventRepository.getEventsByEventCreatorId(testUser.getId()))
                .thenReturn(Optional.of(expectedEvents));
        //when
        List<Event> actualEvents = eventService.getEventsByEventCreatorId(testUser.getId());
        //then
        assertNotNull(actualEvents);
        assertEquals(expectedEvents.size(), actualEvents.size());
        assertTrue(actualEvents.containsAll(expectedEvents));
        assertThat(expectedEvents).usingRecursiveComparison().isEqualTo(actualEvents);
        Mockito.verify(eventRepository).getEventsByEventCreatorId(testUser.getId());
    }

    @Test
    void getEventsByEventCreatorId_WithNonExistingEvents_ThrowsException() {
        //given
        Long nonExistingEventCreatorId = 2L;

        Mockito.when(eventRepository.getEventsByEventCreatorId(nonExistingEventCreatorId))
                .thenReturn(Optional.empty());
        //then
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> eventService.getEventsByEventCreatorId(nonExistingEventCreatorId));

        assertEquals("Wrong user ID or events doesn't exist", exception.getMessage());
        Mockito.verify(eventRepository).getEventsByEventCreatorId(nonExistingEventCreatorId);
    }

    @Test
    void createEvent() {
        //given
        EventRequestDTO eventRequestDTO = modelMapper.map(testEvent, EventRequestDTO.class);

        Mockito.when(eventRepository.save(ArgumentMatchers.any(Event.class))).thenReturn(testEvent);
        //when
        Event actualEvent = eventService.createEvent(testUser, eventRequestDTO);
        //then
        assertEquals(testEvent.getTitle(), actualEvent.getTitle());
        assertEquals(testEvent.getDescription(), actualEvent.getDescription());
        assertEquals(testUser, actualEvent.getEventCreator());
        assertEquals(testEvent.getStartDateTime(), actualEvent.getStartDateTime());
        assertEquals(testEvent.getEndDateTime(), actualEvent.getEndDateTime());
    }

    @Test
    void deleteEvent() {
        //given
        Mockito.when(eventRepository.findById(testEvent.getId())).thenReturn(Optional.of(testEvent));
        //when
        eventService.deleteEvent(testUser, testEvent.getId());
        //then
        Mockito.verify(eventRepository).delete(testEvent);
    }

    @Test
    void deleteEvent_WithInvalidEventId_ThrowsException() {
        //given
        Long invalidEventId = 2L;
        Mockito.when(eventRepository.findById(invalidEventId)).thenReturn(Optional.empty());
        //then
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> eventService.deleteEvent(testUser, invalidEventId));

        assertEquals("Could not find event with ID: " + invalidEventId, exception.getMessage());
    }

    @Test
    void deleteEvent_WithUnauthorizedUser_ShouldThrowException() {
        //given
        User unauthorizedUser = User.builder()
                .id(25L)
                .build();

        Mockito.when(eventRepository.findById(testEvent.getId())).thenReturn(Optional.of(testEvent));
        //then
        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class,
                () -> eventService.deleteEvent(unauthorizedUser, testEvent.getId()));
        assertEquals("Access denied. Wrong event ID", exception.getMessage());
    }
}