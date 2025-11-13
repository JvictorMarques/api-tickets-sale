package com.tcc.api_ticket_sales.application.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventFilterRequestDTO {
    private String name;
    private String location;
    private LocalDateTime dateInitial;
    private LocalDateTime dateFinal;
    private Integer ageRestriction;
    private Boolean available;
}
