package com.tcc.api_ticket_sales.interfaces.dto.ticket;


import com.tcc.api_ticket_sales.interfaces.dto.event.EventResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class TicketCreateResponseDTO {
    private UUID id;

    private String name;

    private String description;

    private int capacity;

    private BigDecimal price;

    private UUID  event;

    private LocalDateTime dateInitial;

    private LocalDateTime dateFinal;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
