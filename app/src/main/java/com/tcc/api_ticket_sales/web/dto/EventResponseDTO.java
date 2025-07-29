package com.tcc.api_ticket_sales.web.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record EventResponseDTO(
        UUID id,
        String name,
        LocalDateTime dateInitial,
        LocalDateTime dateFinal,
        boolean isAdultOnly,
        String category,
        String location,
        Integer maxPeople,
        Long numberTicketsUnavailable,
        Long numberTicketsAvailable
) {
}
