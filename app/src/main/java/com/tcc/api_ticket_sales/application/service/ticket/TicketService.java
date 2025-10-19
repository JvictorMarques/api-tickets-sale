package com.tcc.api_ticket_sales.application.service.ticket;

import com.tcc.api_ticket_sales.interfaces.dto.ticket.TicketCreateRequestDTO;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.TicketCreateResponseDTO;

import java.util.UUID;

public interface TicketService {
    TicketCreateResponseDTO create(UUID eventId, TicketCreateRequestDTO dto);
}
