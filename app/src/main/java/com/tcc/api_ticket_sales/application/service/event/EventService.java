package com.tcc.api_ticket_sales.application.service.event;

import com.tcc.api_ticket_sales.application.dto.event.EventCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventResponseDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventUpdateRequestDTO;

import java.util.UUID;

public interface EventService {

    EventResponseDTO create(EventCreateRequestDTO event);
    EventResponseDTO update(UUID eventId, EventUpdateRequestDTO dto);
    void delete(UUID eventId);
}
