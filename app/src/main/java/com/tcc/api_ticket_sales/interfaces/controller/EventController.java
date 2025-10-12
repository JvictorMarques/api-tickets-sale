package com.tcc.api_ticket_sales.interfaces.controller;

import com.tcc.api_ticket_sales.application.service.event.EventService;
import com.tcc.api_ticket_sales.interfaces.dto.event.EventCreateDTO;
import com.tcc.api_ticket_sales.interfaces.dto.event.EventResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDTO> fromEventCreateToEventEntity (@RequestBody @Valid EventCreateDTO dto){
        EventResponseDTO event = eventService.createEvent(dto);

        URI location = URI.create("/event/" + event.getId());
        return ResponseEntity.created(location).body(event);
    }
}
