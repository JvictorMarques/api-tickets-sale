package com.tcc.api_ticket_sales.application.dto.ticket_type;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class TicketTypeResponseDTO {
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
