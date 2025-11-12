package com.tcc.api_ticket_sales.application.mapper.event;

import com.tcc.api_ticket_sales.application.dto.event.EventUpdateRequestDTO;
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
    void fromEventCreateRequestDTOToEventEntity_shouldReturnEventEntity_whenEventCreateDTO(){
        EventCreateRequestDTO dto = createEventCreateDTOValid();

        EventEntity entity = mapper.fromEventCreateRequestDTOToEventEntity(dto);

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
    void fromEventEntityToEventResponseDTO_shouldReturnEventResponse_whenEventResponseEventEntity(){
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

    @Test
    @Tag("unit")
    void fromEventUpdateRequestDTOToEventEntity_shouldReturnEventEntity_whenEventUpdateRequestDTO(){
        EventUpdateRequestDTO dto = createEventUpdateRequestDTO();
        EventEntity eventEntity = createEventEntityWithId();

        EventEntity entity = mapper.fromEventUpdateRequestDTOToEventEntity(dto, eventEntity);

        assertNotNull(entity);
        assertEquals(eventEntity.getId(), entity.getId());
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
    void fromEventUpdateRequestDTOToEventEntity_shouldReturnEventEntity_whenDtoNameIsNull(){
        EventUpdateRequestDTO dto = createEventUpdateRequestDTO();
        dto.setName(null);
        EventEntity eventEntity = createEventEntityWithId();

        EventEntity response = mapper.fromEventUpdateRequestDTOToEventEntity(dto, eventEntity);

        assertNotNull(response);
        assertEquals(eventEntity.getId(), response.getId());
        assertEquals(eventEntity.getName(), response.getName());
        assertEquals(dto.getDescription(), response.getDescription());
        assertEquals(dto.getDateInitial(), response.getDateInitial());
        assertEquals(dto.getDateFinal(), response.getDateFinal());
        assertEquals(dto.getCapacity(), response.getCapacity());
        assertEquals(dto.getAgeRestriction(), response.getAgeRestriction());
        assertEquals(dto.getLocation(), response.getLocation());
    }


    @Test
    @Tag("unit")
    void fromEventUpdateRequestDTOToEventEntity_shouldReturnEventEntity_whenDtoDescriptionIsNull(){
        EventUpdateRequestDTO dto = createEventUpdateRequestDTO();
        dto.setDescription(null);
        EventEntity eventEntity = createEventEntityWithId();

        EventEntity response = mapper.fromEventUpdateRequestDTOToEventEntity(dto, eventEntity);

        assertNotNull(response);
        assertEquals(eventEntity.getId(), response.getId());
        assertEquals(dto.getName(), response.getName());
        assertEquals(eventEntity.getDescription(), response.getDescription());
        assertEquals(dto.getDateInitial(), response.getDateInitial());
        assertEquals(dto.getDateFinal(), response.getDateFinal());
        assertEquals(dto.getCapacity(), response.getCapacity());
        assertEquals(dto.getAgeRestriction(), response.getAgeRestriction());
        assertEquals(dto.getLocation(), response.getLocation());

    }

    @Test
    @Tag("unit")
    void fromEventUpdateRequestDTOToEventEntity_shouldReturnEventEntity_whenDtoDateInitialIsNull(){
        EventUpdateRequestDTO dto = createEventUpdateRequestDTO();
        dto.setDateInitial(null);
        EventEntity eventEntity = createEventEntityWithId();

        EventEntity response = mapper.fromEventUpdateRequestDTOToEventEntity(dto, eventEntity);

        assertNotNull(response);
        assertEquals(eventEntity.getId(), response.getId());
        assertEquals(dto.getName(), response.getName());
        assertEquals(dto.getDescription(), response.getDescription());
        assertEquals(eventEntity.getDateInitial(), response.getDateInitial());
        assertEquals(dto.getDateFinal(), response.getDateFinal());
        assertEquals(dto.getCapacity(), response.getCapacity());
        assertEquals(dto.getAgeRestriction(), response.getAgeRestriction());
        assertEquals(dto.getLocation(), response.getLocation());
    }

    @Test
    @Tag("unit")
    void fromEventUpdateRequestDTOToEventEntity_shouldReturnEventEntity_whenDtoDateFinalIsNull(){
        EventUpdateRequestDTO dto = createEventUpdateRequestDTO();
        dto.setDateFinal(null);
        EventEntity eventEntity = createEventEntityWithId();

        EventEntity response = mapper.fromEventUpdateRequestDTOToEventEntity(dto, eventEntity);

        assertNotNull(response);
        assertEquals(eventEntity.getId(), response.getId());
        assertEquals(dto.getName(), response.getName());
        assertEquals(dto.getDescription(), response.getDescription());
        assertEquals(dto.getDateInitial(), response.getDateInitial());
        assertEquals(eventEntity.getDateFinal(), response.getDateFinal());
        assertEquals(dto.getCapacity(), response.getCapacity());
        assertEquals(dto.getAgeRestriction(), response.getAgeRestriction());
        assertEquals(dto.getLocation(), response.getLocation());
    }

    @Test
    @Tag("unit")
    void fromEventUpdateRequestDTOToEventEntity_shouldReturnEventEntity_whenDtoLocationIsNull(){
        EventUpdateRequestDTO dto = createEventUpdateRequestDTO();
        dto.setLocation(null);
        EventEntity eventEntity = createEventEntityWithId();

        EventEntity response = mapper.fromEventUpdateRequestDTOToEventEntity(dto, eventEntity);

        assertNotNull(response);
        assertEquals(eventEntity.getId(), response.getId());
        assertEquals(dto.getName(), response.getName());
        assertEquals(dto.getDescription(), response.getDescription());
        assertEquals(dto.getDateInitial(), response.getDateInitial());
        assertEquals(dto.getDateFinal(), response.getDateFinal());
        assertEquals(dto.getCapacity(), response.getCapacity());
        assertEquals(dto.getAgeRestriction(), response.getAgeRestriction());
        assertEquals(eventEntity.getLocation(), response.getLocation());
    }

    @Test
    @Tag("unit")
    void fromEventUpdateRequestDTOToEventEntity_shouldReturnEventEntity_whenDtoCapacityIsNull(){
        EventUpdateRequestDTO dto = createEventUpdateRequestDTO();
        dto.setCapacity(null);
        EventEntity eventEntity = createEventEntityWithId();

        EventEntity response = mapper.fromEventUpdateRequestDTOToEventEntity(dto, eventEntity);

        assertNotNull(response);
        assertEquals(eventEntity.getId(), response.getId());
        assertEquals(dto.getName(), response.getName());
        assertEquals(dto.getDescription(), response.getDescription());
        assertEquals(dto.getDateInitial(), response.getDateInitial());
        assertEquals(dto.getDateFinal(), response.getDateFinal());
        assertEquals(eventEntity.getCapacity(), response.getCapacity());
        assertEquals(dto.getAgeRestriction(), response.getAgeRestriction());
        assertEquals(dto.getLocation(), response.getLocation());
    }

    @Test
    @Tag("unit")
    void fromEventUpdateRequestDTOToEventEntity_shouldReturnEventEntity_whenDtoCapacityBeforeZero(){
        EventUpdateRequestDTO dto = createEventUpdateRequestDTO();
        dto.setCapacity(-1);
        EventEntity eventEntity = createEventEntityWithId();

        EventEntity response = mapper.fromEventUpdateRequestDTOToEventEntity(dto, eventEntity);

        assertNotNull(response);
        assertEquals(eventEntity.getId(), response.getId());
        assertEquals(dto.getName(), response.getName());
        assertEquals(dto.getDescription(), response.getDescription());
        assertEquals(dto.getDateInitial(), response.getDateInitial());
        assertEquals(dto.getDateFinal(), response.getDateFinal());
        assertEquals(eventEntity.getCapacity(), response.getCapacity());
        assertEquals(dto.getAgeRestriction(), response.getAgeRestriction());
        assertEquals(dto.getLocation(), response.getLocation());
    }

    @Test
    @Tag("unit")
    void fromEventUpdateRequestDTOToEventEntity_shouldReturnEventEntity_whenDtoAgeRestrictionIsNull(){
        EventUpdateRequestDTO dto = createEventUpdateRequestDTO();
        dto.setAgeRestriction(null);
        EventEntity eventEntity = createEventEntityWithId();

        EventEntity response = mapper.fromEventUpdateRequestDTOToEventEntity(dto, eventEntity);

        assertNotNull(response);
        assertEquals(eventEntity.getId(), response.getId());
        assertEquals(dto.getName(), response.getName());
        assertEquals(dto.getDescription(), response.getDescription());
        assertEquals(dto.getDateInitial(), response.getDateInitial());
        assertEquals(dto.getDateFinal(), response.getDateFinal());
        assertEquals(dto.getCapacity(), response.getCapacity());
        assertEquals(eventEntity.getAgeRestriction(), response.getAgeRestriction());
        assertEquals(dto.getLocation(), response.getLocation());
    }

    @Test
    @Tag("unit")
    void fromEventUpdateRequestDTOToEventEntity_shouldReturnEventEntity_whenDtoAgeRestrictionBeforeZero(){
        EventUpdateRequestDTO dto = createEventUpdateRequestDTO();
        dto.setAgeRestriction(-1);
        EventEntity eventEntity = createEventEntityWithId();

        EventEntity response = mapper.fromEventUpdateRequestDTOToEventEntity(dto, eventEntity);

        assertNotNull(response);
        assertEquals(eventEntity.getId(), response.getId());
        assertEquals(dto.getName(), response.getName());
        assertEquals(dto.getDescription(), response.getDescription());
        assertEquals(dto.getDateInitial(), response.getDateInitial());
        assertEquals(dto.getDateFinal(), response.getDateFinal());
        assertEquals(dto.getCapacity(), response.getCapacity());
        assertEquals(eventEntity.getAgeRestriction(), response.getAgeRestriction());
        assertEquals(dto.getLocation(), response.getLocation());
    }
}