package com.tcc.api_ticket_sales.application.mapper.event;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.application.dto.event.EventCreateDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Builder
@Component
public class EventMapper {

    public EventEntity fromEventCreateDTOToEventEntity (EventCreateDTO event){
        return EventEntity.of(
                event.getName(),
                event.getDescription(),
                event.getDateInitial(),
                event.getDateFinal(),
                event.getCapacity(),
                event.getAgeRestriction(),
                event.getLocation()
        );

    }

    public EventResponseDTO fromEventEntityToEventResponseDTO (EventEntity event){
        return EventResponseDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .dateInitial(event.getDateInitial())
                .dateFinal(event.getDateFinal())
                .capacity(event.getCapacity())
                .ageRestriction(event.getAgeRestriction())
                .location(event.getLocation())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}
