package com.tcc.api_ticket_sales.application.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventModel {
    private UUID id;
    private String name;
    private LocalDateTime dateInitial;
    private LocalDateTime dateFinal;
    private boolean isAdultOnly;
    private String category;
    private String location;
    private Integer maxPeople;
    private Long numberTicketsUnavailable;
    private Long numberTicketsAvailable;
}
