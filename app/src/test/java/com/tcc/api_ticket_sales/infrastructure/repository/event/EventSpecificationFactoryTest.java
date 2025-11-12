package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.BaseIntegrationTest;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithId;
import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithoutId;
import static org.junit.jupiter.api.Assertions.*;

class EventSpecificationFactoryTest extends BaseIntegrationTest {

    @Autowired
    private EventRepository repository;

    @Autowired
    private EventSpecificationFactory eventSpecificationFactory;

    @Tag("integration")
    @Test
    void findConflictingEvents_shouldReturnEmpty_whenEventNamesAreDifferent(){
        EventEntity eventEntity1 = createEventEntityWithoutId();
        EventEntity eventEntity2 = createEventEntityWithoutId();
        repository.save(eventEntity1);
        eventEntity2.setId(eventEntity1.getId());

        Specification<EventEntity> specification = eventSpecificationFactory.findConflictingEvents(eventEntity2);
        assertTrue(repository.findAll(specification).isEmpty());
    }

    @Tag("integration")
    @Test
    void findConflictingEvents_shouldReturnEmpty_whenEventLocationAreDifferent(){
        EventEntity eventEntity1 = createEventEntityWithoutId();
        EventEntity eventEntity2 = createEventEntityWithoutId();
        eventEntity2.setName(eventEntity1.getName());
        eventEntity2.setLocation("Teste location");
        repository.save(eventEntity1);
        eventEntity2.setId(eventEntity1.getId());

        Specification<EventEntity> specification = eventSpecificationFactory.findConflictingEvents(eventEntity2);
        assertTrue(repository.findAll(specification).isEmpty());
    }

    @Tag("integration")
    @Test
    void findConflictingEvents_shouldReturnEmpty_whenEventIdAreEquals(){
        EventEntity eventEntity1 = createEventEntityWithoutId();
        EventEntity eventEntity2 = createEventEntityWithId();
        eventEntity2.setName(eventEntity1.getName());
        repository.save(eventEntity1);
        eventEntity2.setId(eventEntity1.getId());

        Specification<EventEntity> specification = eventSpecificationFactory.findConflictingEvents(eventEntity2);
        assertTrue(repository.findAll(specification).isEmpty());
    }

    @Tag("integration")
    @Test
    void findConflictingEvents_shouldReturnEmpty_whenDateInitialAndDateFinalNotContainsEvents(){
        EventEntity eventEntity1 = createEventEntityWithoutId();
        EventEntity eventEntity2 = createEventEntityWithoutId();
        eventEntity2.setName(eventEntity1.getName());
        eventEntity2.setDateInitial(eventEntity1.getDateInitial().minusDays(3));
        eventEntity2.setDateFinal(eventEntity1.getDateFinal().minusDays(2));
        repository.save(eventEntity1);
        eventEntity2.setId(eventEntity1.getId());

        Specification<EventEntity> specification = eventSpecificationFactory.findConflictingEvents(eventEntity2);
        assertTrue(repository.findAll(specification).isEmpty());
    }

    @Tag("integration")
    @Test
    void findConflictingEvents_shouldReturnListEvents_whenExistsEventsEquals(){
        EventEntity eventEntity1 = createEventEntityWithoutId();
        EventEntity eventEntity2 = createEventEntityWithId();
        eventEntity2.setName(eventEntity1.getName());
        repository.save(eventEntity1);

        Specification<EventEntity> specification = eventSpecificationFactory.findConflictingEvents(eventEntity2);
        assertEquals(1, repository.findAll(specification).size());
    }

    @Tag("integration")
    @Test
    void findConflictingEvents_shouldReturnListEvents_whenExistsEventsEqualsWithoutId(){
        EventEntity eventEntity1 = createEventEntityWithoutId();
        EventEntity eventEntity2 = createEventEntityWithoutId();
        eventEntity2.setName(eventEntity1.getName());
        repository.save(eventEntity1);

        Specification<EventEntity> specification = eventSpecificationFactory.findConflictingEvents(eventEntity2);
        assertEquals(1, repository.findAll(specification).size());
    }

}