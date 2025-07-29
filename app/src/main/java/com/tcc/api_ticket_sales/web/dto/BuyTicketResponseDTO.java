package com.tcc.api_ticket_sales.web.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record BuyTicketResponseDTO(
        String eventName,
        LocalDateTime dateInitial,
        LocalDateTime dateFinal
) {
}
