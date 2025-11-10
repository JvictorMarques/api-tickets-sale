package com.tcc.api_ticket_sales.application.service.event;

import com.tcc.api_ticket_sales.application.dto.event.EventCreateDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventResponseDTO;

public interface EventService {

    EventResponseDTO createEvent(EventCreateDTO event);
}
