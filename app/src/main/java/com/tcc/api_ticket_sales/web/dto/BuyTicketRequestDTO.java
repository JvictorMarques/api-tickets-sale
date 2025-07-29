package com.tcc.api_ticket_sales.web.dto;

import java.util.UUID;

public record BuyTicketRequestDTO(
        UUID ticketId,
        UUID holderId
) {
}
