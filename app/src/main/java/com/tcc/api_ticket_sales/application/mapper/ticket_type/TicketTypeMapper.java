package com.tcc.api_ticket_sales.application.mapper.ticket_type;

import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeUpdateRequestDTO;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeResponseDTO;
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

    public TicketTypeResponseDTO fromTicketTypeEntityToTicketTypeResponseDTO(TicketTypeEntity entity) {
        return TicketTypeResponseDTO.builder()
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

    public TicketTypeEntity fromTicketTypeUpdateRequestDTOToTicketTypeEntity(TicketTypeUpdateRequestDTO dto, TicketTypeEntity ticketTypeEntityExists) {
        if (dto.getName() != null) ticketTypeEntityExists.setName(dto.getName());
        if (dto.getDescription() != null) ticketTypeEntityExists.setDescription(dto.getDescription());
        if (dto.getPrice() != null) ticketTypeEntityExists.setPrice(dto.getPrice());
        if (dto.getCapacity() != null) ticketTypeEntityExists.setCapacity(dto.getCapacity());
        if (dto.getDateInitial() != null) ticketTypeEntityExists.setDateInitial(dto.getDateInitial());
        if (dto.getDateFinal() != null) ticketTypeEntityExists.setDateFinal(dto.getDateFinal());

        return ticketTypeEntityExists;
    }
}
