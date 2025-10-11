package com.tcc.api_ticket_sales.interfaces.mapper.ticket;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.TicketCreateRequestDTO;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.TicketCreateResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@Component
public class TicketMapper {

    public TicketEntity fromTicketCreateRequestDTOToTicketEntity(TicketCreateRequestDTO dto, UUID eventId) {
        EventEntity eventEntity= EventEntity.reference(eventId);
        return TicketEntity.of(
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                eventEntity,
                dto.getCapacity(),
                dto.getDateInitial(),
                dto.getDateFinal()
        );
    }

    public TicketCreateResponseDTO fromTicketEntityToTicketCreateResponseDTO(TicketEntity entity) {
        return TicketCreateResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .capacity(entity.getCapacity())
                .dateInitial(entity.getDateInitial())
                .dateFinal(entity.getDateFinal())
                .event(entity.getEventEntity().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
