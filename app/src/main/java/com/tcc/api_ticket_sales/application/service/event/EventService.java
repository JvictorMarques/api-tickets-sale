package com.tcc.api_ticket_sales.application.service.event;

import com.tcc.api_ticket_sales.application.dto.event.EventCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventFilterRequestDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventResponseDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventUpdateRequestDTO;

import java.util.List;
import java.util.UUID;

public interface EventService {

    EventResponseDTO create(EventCreateRequestDTO event);
    EventResponseDTO update(UUID eventId, EventUpdateRequestDTO dto);
    void delete(UUID eventId);
    List<EventResponseDTO> getByParams(EventFilterRequestDTO filter);
    EventResponseDTO getById(UUID eventId);
}
