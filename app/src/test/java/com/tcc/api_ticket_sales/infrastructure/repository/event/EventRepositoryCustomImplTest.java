package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.BaseIntegrationTest;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.application.dto.event.EventCreateRequestDTO;
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
    void checkExists_shouldReturnIsNotEmpty_whenEventExists() {
        EventEntity eventEntity = createEventEntityWithoutId();
        entityManager.persist(eventEntity);

        assertFalse(repository.checkExists(
                eventEntity.getName(),
                eventEntity.getLocation(),
                eventEntity.getDateInitial().minusMinutes(20),
                eventEntity.getDateFinal().minusMinutes(10)
        ).isEmpty());
    }

    @Test
    @Tag("integration")
    void checkExists_shouldReturnIsEmpty_whenLocationIsDifferent() {
        EventEntity eventEntity = createEventEntityWithoutId();
        entityManager.persist(eventEntity);

        EventCreateRequestDTO eventCreateRequestDTO = createEventCreateDTODefault();
        eventCreateRequestDTO.setName(eventEntity.getName());
        eventCreateRequestDTO.setDateInitial(eventEntity.getDateInitial().minusMinutes(20));
        eventCreateRequestDTO.setDateFinal(eventEntity.getDateFinal().minusMinutes(10));

        assertTrue(repository.checkExists(
                eventEntity.getName(),
                "Teste",
                eventEntity.getDateInitial(),
                eventEntity.getDateFinal()
        ).isEmpty());
    }

    @Test
    @Tag("integration")
    void checkExists_shouldReturnIsEmpty_whenNameIsDifferent() {
        EventEntity eventEntity = createEventEntityWithoutId();
        entityManager.persist(eventEntity);

        EventCreateRequestDTO eventCreateRequestDTO = createEventCreateDTODefault();
        eventCreateRequestDTO.setLocation(eventEntity.getLocation());
        eventCreateRequestDTO.setDateInitial(eventEntity.getDateInitial().minusMinutes(20));
        eventCreateRequestDTO.setDateFinal(eventEntity.getDateFinal().minusMinutes(10));

        assertTrue(repository.checkExists(
                "Teste",
                eventEntity.getLocation(),
                eventEntity.getDateInitial(),
                eventEntity.getDateFinal()
        ).isEmpty());
    }

    @Test
    @Tag("integration")
    void checkExists_shouldReturnIsEmpty_whenDatesAreDifferent() {
        EventEntity eventEntity = createEventEntityWithoutId();
        entityManager.persist(eventEntity);

        EventCreateRequestDTO eventCreateRequestDTO = createEventCreateDTODefault();
        eventCreateRequestDTO.setName(eventEntity.getName());
        eventCreateRequestDTO.setLocation(eventEntity.getLocation());
        eventCreateRequestDTO.setDateInitial(eventEntity.getDateInitial().plusDays(4));
        eventCreateRequestDTO.setDateFinal(eventEntity.getDateFinal().plusDays(5));

        assertTrue(repository.checkExists(
                eventEntity.getName(),
                eventEntity.getLocation(),
                eventEntity.getDateInitial().plusDays(4),
                eventEntity.getDateFinal().plusDays(5)
        ).isEmpty());
    }
}