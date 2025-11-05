package com.tcc.api_ticket_sales.application.mapper.event;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.application.dto.event.EventCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


import static com.tcc.api_ticket_sales.factory.EventFactory.*;
import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {
    private EventMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new EventMapper();
    }

    @Test
    @Tag("unit")
    void shouldMapEventCreateDTOToEventEntity(){
        EventCreateRequestDTO dto = createEventCreateDTOValid();

        EventEntity entity = mapper.fromEventCreateDTOToEventEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getDateInitial(), entity.getDateInitial());
        assertEquals(dto.getDateFinal(), entity.getDateFinal());
        assertEquals(dto.getCapacity(), entity.getCapacity());
        assertEquals(dto.getAgeRestriction(), entity.getAgeRestriction());
        assertEquals(dto.getLocation(), entity.getLocation());
    }

    @Test
    @Tag("unit")
    void shouldMapEventEntityToEventResponse(){
        EventEntity entity = createEventEntityWithId();

        EventResponseDTO dto = mapper.fromEventEntityToEventResponseDTO(entity);

        assertNotNull(dto);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getDateInitial(), entity.getDateInitial());
        assertEquals(dto.getDateFinal(), entity.getDateFinal());
        assertEquals(dto.getCapacity(), entity.getCapacity());
        assertEquals(dto.getAgeRestriction(), entity.getAgeRestriction());
        assertEquals(dto.getLocation(), entity.getLocation());
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), entity.getUpdatedAt());
    }
}