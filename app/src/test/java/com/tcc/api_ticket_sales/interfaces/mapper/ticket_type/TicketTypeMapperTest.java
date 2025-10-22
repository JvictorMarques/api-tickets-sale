package com.tcc.api_ticket_sales.interfaces.mapper.ticket_type;

import com.tcc.api_ticket_sales.application.mapper.ticket_type.TicketTypeMapper;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.factory.EventFactory;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateResponseDTO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeCreateRequestDTOValid;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeEntityWithId;
import static org.junit.jupiter.api.Assertions.*;

class TicketTypeMapperTest {
    @Test
    @Tag("unit")
    void shouldMapTicketTypeCreateRequestDTOToTicketEntity(){
        TicketTypeMapper mapper= new TicketTypeMapper();
        TicketTypeCreateRequestDTO dto = createTicketTypeCreateRequestDTOValid();
        EventEntity eventEntity = EventFactory.createEventEntityWithId();

        TicketTypeEntity entity = mapper.fromTicketTypeCreateRequestDTOToTicketTypeEntity(dto, eventEntity.getId());

        assertNotNull(entity);
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getDateInitial(), entity.getDateInitial());
        assertEquals(dto.getDateFinal(), entity.getDateFinal());
        assertEquals(dto.getCapacity(), entity.getCapacity());
        assertEquals(dto.getPrice(), entity.getPrice());
        assertEquals(eventEntity.getId(), entity.getEventEntity().getId());
    }

    @Test
    @Tag("unit")
    void shouldMapTicketTypeEntityToTicketCreateResponseDTO(){
        TicketTypeEntity entity = createTicketTypeEntityWithId();
        TicketTypeMapper mapper= new TicketTypeMapper();

        TicketTypeCreateResponseDTO dto = mapper.fromTicketTypeEntityToTicketTypeCreateResponseDTO(entity);

        assertNotNull(dto);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getDateInitial(), entity.getDateInitial());
        assertEquals(dto.getDateFinal(), entity.getDateFinal());
        assertEquals(dto.getCapacity(), entity.getCapacity());
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), entity.getUpdatedAt());
    }

}