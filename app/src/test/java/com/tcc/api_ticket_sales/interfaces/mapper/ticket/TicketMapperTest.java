package com.tcc.api_ticket_sales.interfaces.mapper.ticket;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.factory.EventFactory;
import com.tcc.api_ticket_sales.interfaces.dto.event.EventResponseDTO;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.TicketCreateRequestDTO;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.TicketCreateResponseDTO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithId;
import static com.tcc.api_ticket_sales.factory.TicketFactory.createTicketCreateRequestDTOValid;
import static com.tcc.api_ticket_sales.factory.TicketFactory.createTicketEntityWithId;
import static org.junit.jupiter.api.Assertions.*;

class TicketMapperTest {
    @Test
    @Tag("unit")
    void shouldMapTicketCreateRequestDTOToTicketEntity(){
        TicketMapper mapper= new TicketMapper();
        TicketCreateRequestDTO dto = createTicketCreateRequestDTOValid();
        EventEntity eventEntity = EventFactory.createEventEntityWithId();

        TicketEntity entity = mapper.fromTicketCreateRequestDTOToTicketEntity(dto, eventEntity.getId());

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
    void shouldMapTicketEntityToTicketCreateResponseDTO(){
        TicketEntity entity = createTicketEntityWithId();
        TicketMapper mapper= new TicketMapper();

        TicketCreateResponseDTO dto = mapper.fromTicketEntityToTicketCreateResponseDTO(entity);

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