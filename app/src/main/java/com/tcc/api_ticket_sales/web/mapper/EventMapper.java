package com.tcc.api_ticket_sales.web.mapper;

import com.tcc.api_ticket_sales.application.model.EventModel;
import com.tcc.api_ticket_sales.web.dto.EventResponseDTO;

public class EventMapper {
    public static EventResponseDTO fromEventModelToEventModelResponseDTO (EventModel model){
        return EventResponseDTO.builder()
                .id(model.getId())
                .name(model.getName())
                .dateInitial(model.getDateInitial())
                .dateFinal(model.getDateFinal())
                .isAdultOnly(model.isAdultOnly())
                .category(model.getCategory())
                .location(model.getLocation())
                .maxPeople(model.getMaxPeople())
                .numberTicketsUnavailable(model.getNumberTicketsUnavailable())
                .numberTicketsAvailable(model.getNumberTicketsAvailable())
                .build();
    }
}
