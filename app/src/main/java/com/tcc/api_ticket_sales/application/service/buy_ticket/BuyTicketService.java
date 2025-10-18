package com.tcc.api_ticket_sales.application.service.buy_ticket;

import com.tcc.api_ticket_sales.interfaces.dto.buy_ticket.BuyTicketRequestDTO;
import com.tcc.api_ticket_sales.interfaces.dto.buy_ticket.BuyTicketResponseDTO;

public interface BuyTicketService {
    BuyTicketResponseDTO buyTicket(BuyTicketRequestDTO buyTicketRequestDTO);
}
