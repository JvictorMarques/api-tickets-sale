package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.application.model.EventModel;
import com.tcc.api_ticket_sales.web.dto.FilterEventRequestDTO;

import java.util.List;
import java.util.UUID;

public interface EventRepositoryCustom {
    List<EventModel> getEventsByFilter(FilterEventRequestDTO filter);

    Long getTicketsAvailableByEventId(UUID eventId);
}
