package com.tcc.api_ticket_sales.application.mapper.ticket_type;

import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeUpdateRequestDTO;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.factory.EventFactory;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeResponseDTO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeCreateRequestDTOValid;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeEntityWithId;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeUpdateRequestDTODefault;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TicketTypeMapperTest {
    @InjectMocks
    private TicketTypeMapper mapper;

    @Test
    @Tag("unit")
    void shouldMapTicketTypeCreateRequestDTOToTicketEntity(){
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
    void shouldMapTicketTypeEntityToTicketResponseDTO(){
        TicketTypeEntity entity = createTicketTypeEntityWithId();

        TicketTypeResponseDTO dto = mapper.fromTicketTypeEntityToTicketTypeResponseDTO(entity);

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

    @Test
    @Tag("unit")
    void shouldMapTicketTypeUpdateRequestDTOToTicketTypeEntity() {
        TicketTypeUpdateRequestDTO dto = createTicketTypeUpdateRequestDTODefault();
        TicketTypeEntity entity = createTicketTypeEntityWithId();

        TicketTypeEntity result = mapper.fromTicketTypeUpdateRequestDTOToTicketTypeEntity(dto, entity);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getDateInitial(), result.getDateInitial());
        assertEquals(dto.getDateFinal(), result.getDateFinal());
        assertEquals(dto.getCapacity(), result.getCapacity());
        assertEquals(dto.getPrice(), result.getPrice());
    }

    @Test
    @Tag("unit")
    void shouldMapTicketTypeUpdateRequestDTOToTicketTypeEntity_whenTicketTypeUpdateRequestDTOHasFieldsNull() {
        TicketTypeUpdateRequestDTO dto = createTicketTypeUpdateRequestDTODefault();
        dto.setName(null);
        dto.setDescription(null);
        dto.setDateFinal(null);
        TicketTypeEntity entity = createTicketTypeEntityWithId();

        TicketTypeEntity result = mapper.fromTicketTypeUpdateRequestDTOToTicketTypeEntity(dto, entity);

        assertNotNull(result);
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getDescription(), result.getDescription());
        assertEquals(dto.getDateInitial(), result.getDateInitial());
        assertEquals(entity.getDateFinal(), result.getDateFinal());
        assertEquals(dto.getCapacity(), result.getCapacity());
        assertEquals(dto.getPrice(), result.getPrice());
    }
}