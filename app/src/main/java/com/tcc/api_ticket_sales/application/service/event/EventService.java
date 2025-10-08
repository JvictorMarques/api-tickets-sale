package com.tcc.api_ticket_sales.application.service.event;

import com.tcc.api_ticket_sales.interfaces.dto.event.EventCreateDTO;
import com.tcc.api_ticket_sales.interfaces.dto.event.EventResponseDTO;

public interface EventService {

    public EventResponseDTO createEvent(EventCreateDTO event);
}
