package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.BaseIntegrationTest;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithId;
import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithoutId;
import static org.junit.jupiter.api.Assertions.*;

class EventSpecificationTest extends BaseIntegrationTest {

    @Autowired
    private EventRepository repository;

    @Tag("integration")
    @Test
    void nameEquals_shouldReturnListIsEmpty_whenNameIsNull(){
        Specification<EventEntity> specification = EventSpecification.nameEquals(null);
        assertTrue(repository.findAll(specification).isEmpty());
    }

    @Tag("integration")
    @Test
    void nameEquals_shouldReturnListIsEmpty_whenEventNameNotEqualsName(){
        EventEntity eventEntity1 = createEventEntityWithoutId();
        EventEntity eventEntity2 = createEventEntityWithoutId();
        repository.save(eventEntity1);

        Specification<EventEntity> specification = EventSpecification.nameEquals(eventEntity2.getName());
        assertTrue(repository.findAll(specification).isEmpty());
    }

    @Tag("integration")
    @Test
    void nameEquals_shouldReturnListEventEntity_whenEventsNameEqualsName(){
        EventEntity eventEntity = createEventEntityWithoutId();
        repository.save(eventEntity);
        Specification<EventEntity> specification = EventSpecification.nameEquals(eventEntity.getName());

        List<EventEntity> eventEntities = repository.findAll(specification);

        assertFalse(eventEntities.isEmpty());
        assertEquals(1, eventEntities.size());
    }


    @Tag("integration")
    @Test
    void locationEquals_shouldReturnListIsEmpty_whenLocationIsNull(){
        Specification<EventEntity> specification = EventSpecification.locationEquals(null);
        assertTrue(repository.findAll(specification).isEmpty());
    }

    @Tag("integration")
    @Test
    void locationEquals_shouldReturnListIsEmpty_whenEventsLocationNotEqualsLocation(){
        EventEntity eventEntity1 = createEventEntityWithoutId();
        EventEntity eventEntity2 = createEventEntityWithoutId();
        eventEntity2.setLocation("Teste Location");
        repository.save(eventEntity1);

        Specification<EventEntity> specification = EventSpecification.locationEquals(eventEntity2.getLocation());
        assertTrue(repository.findAll(specification).isEmpty());
    }

    @Tag("integration")
    @Test
    void locationEquals_shouldReturnListEventEntity_whenEventsLocationEqualsLocation(){
        EventEntity eventEntity = createEventEntityWithoutId();
        repository.save(eventEntity);
        Specification<EventEntity> specification = EventSpecification.locationEquals(eventEntity.getLocation());

        List<EventEntity> eventEntities = repository.findAll(specification);

        assertFalse(eventEntities.isEmpty());
        assertEquals(1, eventEntities.size());
    }

    @Tag("integration")
    @Test
    void dateBetween_shouldReturnListIsEmpty_whenDateInitialIsNull(){
        Specification<EventEntity> specification = EventSpecification.dateBetween(null, LocalDateTime.now());
        assertTrue(repository.findAll(specification).isEmpty());
    }

    @Tag("integration")
    @Test
    void dateBetween_shouldReturnListIsEmpty_whenDateFinalIsNull(){
        Specification<EventEntity> specification = EventSpecification.dateBetween(LocalDateTime.now(), null);
        assertTrue(repository.findAll(specification).isEmpty());
    }

    @Tag("integration")
    @Test
    void dateBetween_shouldReturnListIsEmpty_whenDateInitialAndDateFinalIsNull(){
        Specification<EventEntity> specification = EventSpecification.dateBetween(null, null);
        assertTrue(repository.findAll(specification).isEmpty());
    }

    @Tag("integration")
    @Test
    void dateBetween_shouldReturnListIsEmpty_whenDateInitialAndDateFinalNotContainsEvents(){
        EventEntity eventEntity1 = createEventEntityWithoutId();
        eventEntity1.setDateInitial(LocalDateTime.now().minusDays(30));
        eventEntity1.setDateFinal(LocalDateTime.now().minusDays(30));

        EventEntity eventEntity2 = createEventEntityWithoutId();
        repository.save(eventEntity1);

        Specification<EventEntity> specification = EventSpecification.dateBetween(eventEntity2.getDateInitial(), eventEntity2.getDateFinal());
        assertTrue(repository.findAll(specification).isEmpty());
    }

    @Tag("integration")
    @Test
    void dateBetween_shouldReturnListIsEmpty_whenDateInitialAndDateFinalContainsEvents(){
        EventEntity eventEntity1 = createEventEntityWithoutId();
        EventEntity eventEntity2 = createEventEntityWithoutId();
        repository.saveAll(List.of(eventEntity1, eventEntity2));

        Specification<EventEntity> specification = EventSpecification.dateBetween(eventEntity2.getDateInitial(), eventEntity2.getDateFinal());
        List<EventEntity> eventEntities = repository.findAll(specification);

        assertFalse(eventEntities.isEmpty());
        assertEquals(2, eventEntities.size());
    }

    @Tag("integration")
    @Test
    void idEquals_shouldReturnListIsEmpty_whenIdIsNull(){
        Specification<EventEntity> specification = EventSpecification.idEquals(null);
        assertTrue(repository.findAll(specification).isEmpty());
    }

    @Tag("integration")
    @Test
    void idEquals_shouldReturnListIsEmpty_whenEventIdNotEqualsId(){
        EventEntity eventEntity1 = createEventEntityWithoutId();
        EventEntity eventEntity2 = createEventEntityWithId();
        repository.save(eventEntity1);

        Specification<EventEntity> specification = EventSpecification.idEquals(eventEntity2.getId());
        assertTrue(repository.findAll(specification).isEmpty());
    }

    @Tag("integration")
    @Test
    void idEquals_shouldReturnListEventEntity_whenEventsIdEqualsId(){
        EventEntity eventEntity = createEventEntityWithoutId();
        repository.save(eventEntity);
        Specification<EventEntity> specification = EventSpecification.idEquals(eventEntity.getId());

        List<EventEntity> eventEntities = repository.findAll(specification);

        assertFalse(eventEntities.isEmpty());
        assertEquals(1, eventEntities.size());
    }
}