package com.tcc.api_ticket_sales.interfaces.dto.event;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class EventResponseDTO {

    private UUID id;

    private String name;

    private String description;

    private String location;

    private int capacity;

    private int ageRestriction;

    private LocalDateTime dateInitial;

    private LocalDateTime dateFinal;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
