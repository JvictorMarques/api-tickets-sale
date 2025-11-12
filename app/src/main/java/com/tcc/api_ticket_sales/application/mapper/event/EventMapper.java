package com.tcc.api_ticket_sales.application.mapper.event;

import com.tcc.api_ticket_sales.application.dto.event.EventUpdateRequestDTO;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.application.dto.event.EventCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Builder
@Component
public class EventMapper {

    public EventEntity fromEventCreateRequestDTOToEventEntity(EventCreateRequestDTO event){
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

    public EventEntity fromEventUpdateRequestDTOToEventEntity(EventUpdateRequestDTO dto, EventEntity eventExists) {
        EventEntity newEventEntity = EventEntity.reference(eventExists.getId());

        newEventEntity.setName(dto.getName() != null ? dto.getName() : eventExists.getName());
        newEventEntity.setDescription(dto.getDescription() != null ? dto.getDescription() : eventExists.getDescription());
        newEventEntity.setDateInitial(dto.getDateInitial() != null ? dto.getDateInitial() : eventExists.getDateInitial());
        newEventEntity.setDateFinal(dto.getDateFinal() != null ? dto.getDateFinal() : eventExists.getDateFinal());
        newEventEntity.setCapacity((dto.getCapacity() != null && dto.getCapacity() > 0)
                ? dto.getCapacity()
                : eventExists.getCapacity()
        );
        newEventEntity.setAgeRestriction((dto.getAgeRestriction() != null && dto.getAgeRestriction() > 0)
                ? dto.getAgeRestriction()
                : eventExists.getAgeRestriction()
        );
        newEventEntity.setLocation(dto.getLocation() != null ? dto.getLocation() : eventExists.getLocation());

        return newEventEntity;
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
