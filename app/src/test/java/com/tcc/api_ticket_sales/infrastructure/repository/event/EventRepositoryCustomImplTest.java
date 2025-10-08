package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.BaseIntegrationTest;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.interfaces.dto.event.EventCreateDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tcc.api_ticket_sales.factory.EventFactory.createEventCreateDTODefault;
import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithoutId;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventRepositoryCustomImplTest extends BaseIntegrationTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EventRepositoryCustomImpl repository;

    @Test
    @Tag("integration")
    protected void checkExists_shouldReturnTrue_whenEventExists() {
        EventEntity eventEntity = createEventEntityWithoutId();
        entityManager.persist(eventEntity);

        EventCreateDTO eventCreateDTO = createEventCreateDTODefault();
        eventCreateDTO.setName(eventEntity.getName());
        eventCreateDTO.setLocation(eventEntity.getLocation());
        eventCreateDTO.setDateInitial(eventEntity.getDateInitial().minusMinutes(20));
        eventCreateDTO.setDateFinal(eventEntity.getDateFinal().minusMinutes(10));

        assertTrue(repository.checkExists(eventCreateDTO));
    }

    @Test
    @Tag("integration")
    protected void checkExists_shouldReturnFalse_whenLocationIsDifferent() {
        EventEntity eventEntity = createEventEntityWithoutId();
        entityManager.persist(eventEntity);

        EventCreateDTO eventCreateDTO = createEventCreateDTODefault();
        eventCreateDTO.setName(eventEntity.getName());
        eventCreateDTO.setDateInitial(eventEntity.getDateInitial().minusMinutes(20));
        eventCreateDTO.setDateFinal(eventEntity.getDateFinal().minusMinutes(10));

        assertFalse(repository.checkExists(eventCreateDTO));
    }

    @Test
    @Tag("integration")
    protected void checkExists_shouldReturnFalse_whenNameIsDifferent() {
        EventEntity eventEntity = createEventEntityWithoutId();
        entityManager.persist(eventEntity);

        EventCreateDTO eventCreateDTO = createEventCreateDTODefault();
        eventCreateDTO.setLocation(eventEntity.getLocation());
        eventCreateDTO.setDateInitial(eventEntity.getDateInitial().minusMinutes(20));
        eventCreateDTO.setDateFinal(eventEntity.getDateFinal().minusMinutes(10));

        assertFalse(repository.checkExists(eventCreateDTO));
    }

    @Test
    @Tag("integration")
    protected void checkExists_shouldReturnFalse_whenDatesAreDifferent() {
        EventEntity eventEntity = createEventEntityWithoutId();
        entityManager.persist(eventEntity);

        EventCreateDTO eventCreateDTO = createEventCreateDTODefault();
        eventCreateDTO.setName(eventEntity.getName());
        eventCreateDTO.setLocation(eventEntity.getLocation());
        eventCreateDTO.setDateInitial(eventEntity.getDateInitial().plusDays(1));
        eventCreateDTO.setDateFinal(eventEntity.getDateFinal().plusDays(2));

        assertFalse(repository.checkExists(eventCreateDTO));
    }
}