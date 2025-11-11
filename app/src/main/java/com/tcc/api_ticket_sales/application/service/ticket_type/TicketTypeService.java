package com.tcc.api_ticket_sales.application.service.ticket_type;

import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeResponseDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeUpdateRequestDTO;

import java.util.UUID;

public interface TicketTypeService {
    TicketTypeResponseDTO create(UUID eventId, TicketTypeCreateRequestDTO dto);
    TicketTypeResponseDTO update(UUID ticketId, TicketTypeUpdateRequestDTO dto);
}
