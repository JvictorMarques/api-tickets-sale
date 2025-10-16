package com.tcc.api_ticket_sales.interfaces.mapper.ticket_type;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.interfaces.dto.ticket_type.TicketTypeCreateRequestDTO;
import com.tcc.api_ticket_sales.interfaces.dto.ticket_type.TicketTypeCreateResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@Component
public class TicketTypeMapper {

    public TicketTypeEntity fromTicketTypeCreateRequestDTOToTicketTypeEntity(TicketTypeCreateRequestDTO dto, UUID eventId) {
        EventEntity eventEntity= EventEntity.reference(eventId);
        return TicketTypeEntity.of(
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                eventEntity,
                dto.getCapacity(),
                dto.getDateInitial(),
                dto.getDateFinal()
        );
    }

    public TicketTypeCreateResponseDTO fromTicketTypeEntityToTicketTypeCreateResponseDTO(TicketTypeEntity entity) {
        return TicketTypeCreateResponseDTO.builder()
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
