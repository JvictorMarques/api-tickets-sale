package com.tcc.api_ticket_sales.domain.interfaces;

import com.tcc.api_ticket_sales.interfaces.dto.ticket.BuyTicketRequestDTO;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.BuyTicketResponseDTO;

public interface PaymentGateway {
    BuyTicketResponseDTO createPayment (BuyTicketRequestDTO buyTicketRequestDTO);
}
