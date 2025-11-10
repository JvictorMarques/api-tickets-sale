package com.tcc.api_ticket_sales.application.service.ticket_type;

import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateResponseDTO;

import java.util.UUID;

public interface TicketTypeService {
    TicketTypeCreateResponseDTO create(UUID eventId, TicketTypeCreateRequestDTO dto);
}
