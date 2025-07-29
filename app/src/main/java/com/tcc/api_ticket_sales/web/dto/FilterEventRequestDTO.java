package com.tcc.api_ticket_sales.web.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record FilterEventRequestDTO(
    LocalDate dateInitial,
    LocalDate dateFinal,
    List<UUID> categoriesId,
    List<UUID> locationsId
){}
