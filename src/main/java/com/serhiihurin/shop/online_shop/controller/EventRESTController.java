package com.serhiihurin.shop.online_shop.controller;

import com.serhiihurin.shop.online_shop.dto.EventRequestDTO;
import com.serhiihurin.shop.online_shop.dto.EventResponseDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.facades.interfaces.EventFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-shop/events")
@PreAuthorize("hasAnyRole('ADMIN', 'SHOP_OWNER', 'SUPER_ADMIN')")
@Tag(name = "Event")
@RequiredArgsConstructor
public class EventRESTController {
    private final EventFacade eventFacade;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('product management', 'service information management')")
    public ResponseEntity<List<EventResponseDTO>> getEventsByEventCreatorId(User currentAuthenticatedUser) {
        return ResponseEntity.ok(
                modelMapper.map(
                        eventFacade.getEventsByEventCreatorId(currentAuthenticatedUser.getId()),
                        new TypeToken<List<EventResponseDTO>>() {
                        }.getType()
                )
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('product management', 'service information management')")
    public ResponseEntity<EventResponseDTO> createEvent(
            User currentAuthenticatedUser,
            @RequestBody EventRequestDTO eventRequestDTO
    ) {
        return ResponseEntity.ok(
                modelMapper.map(
                        eventFacade.createEvent(currentAuthenticatedUser, eventRequestDTO),
                        EventResponseDTO.class
                )
        );
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAnyAuthority('product management', 'service information management')")
    public ResponseEntity<Void> deleteEvent(User currentAuthenticatedUser, @PathVariable Long eventId) {
        eventFacade.deleteEvent(currentAuthenticatedUser, eventId);
        return ResponseEntity.ok().build();
    }
}
