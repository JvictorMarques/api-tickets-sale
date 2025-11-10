package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.BaseIntegrationTest;
import com.tcc.api_ticket_sales.application.dto.event.EventFilterRequestDTO;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentStatusRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.holder.HolderRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.order.OrderRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithId;
import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithoutId;
import static com.tcc.api_ticket_sales.factory.TicketFactory.createTicketEntityPaymentApproved;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeEntityWithoutId;
import static org.junit.jupiter.api.Assertions.*;

class EventSpecificationFactoryIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private EventRepository repository;

    @Autowired
    private HolderRepository holderRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

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

    @Tag("integration")
    @Test
    void findConflictingEvents_shouldReturnListEmpty_whenEventDeleted(){
        EventEntity eventEntity1 = createEventEntityWithoutId();
        EventEntity eventEntity2 = createEventEntityWithoutId();
        eventEntity2.setName(eventEntity1.getName());
        eventEntity1.setDeletedAt(LocalDateTime.now());
        repository.save(eventEntity1);

        Specification<EventEntity> specification = eventSpecificationFactory.findConflictingEvents(eventEntity2);
        assertTrue(repository.findAll(specification).isEmpty());
    }

    @Tag("integration")
    @Test
    void findFilter_shouldReturnListEmpty_whenNameDoesNotMatch() {
        EventEntity event = createEventEntityWithoutId();
        event.setName("Rock in Rio");
        repository.save(event);

        EventFilterRequestDTO filter = new EventFilterRequestDTO();
        filter.setName("Lollapalooza");

        Specification<EventEntity> spec = eventSpecificationFactory.findFilter(filter);
        List<EventEntity> result = repository.findAll(spec);

        assertTrue(result.isEmpty());
    }

    @Tag("integration")
    @Test
    void findFilter_shouldReturnListEventEntity_whenNameMatchesPartially() {
        EventEntity event = createEventEntityWithoutId();
        event.setName("Festival de Música");
        repository.save(event);

        EventFilterRequestDTO filter = new EventFilterRequestDTO();
        filter.setName("Música");

        Specification<EventEntity> spec = eventSpecificationFactory.findFilter(filter);
        List<EventEntity> result = repository.findAll(spec);

        assertFalse(result.isEmpty());
    }

    @Tag("integration")
    @Test
    void findFilter_shouldReturnListEmpty_whenLocationDoesNotMatch() {
        EventEntity event = createEventEntityWithoutId();
        event.setLocation("São Paulo");
        repository.save(event);

        EventFilterRequestDTO filter = new EventFilterRequestDTO();
        filter.setLocation("Rio de Janeiro");

        Specification<EventEntity> spec = eventSpecificationFactory.findFilter(filter);
        List<EventEntity> result = repository.findAll(spec);

        assertTrue(result.isEmpty());
    }

    @Tag("integration")
    @Test
    void findFilter_shouldReturnListEventEntity_whenDateIsBetweenInitialAndFinal() {
        EventEntity eventEntity = createEventEntityWithoutId();
        repository.save(eventEntity);

        EventFilterRequestDTO filter = new EventFilterRequestDTO();
        filter.setDateInitial(eventEntity.getDateInitial());
        filter.setDateFinal(eventEntity.getDateFinal());

        Specification<EventEntity> spec = eventSpecificationFactory.findFilter(filter);
        List<EventEntity> result = repository.findAll(spec);

        assertFalse(result.isEmpty());
    }

    @Tag("integration")
    @Test
    void findFilter_shouldReturnListEmpty_whenDateOutsideRange() {
        EventEntity event = createEventEntityWithoutId();
        repository.save(event);

        EventFilterRequestDTO filter = new EventFilterRequestDTO();
        filter.setDateInitial(event.getDateInitial().minusDays(3));
        filter.setDateFinal(event.getDateInitial().minusDays(2));

        Specification<EventEntity> spec = eventSpecificationFactory.findFilter(filter);
        List<EventEntity> result = repository.findAll(spec);

        assertTrue(result.isEmpty());
    }

    @Tag("integration")
    @Test
    void findFilter_shouldReturnListEventEntity_whenAgeRestrictionIsLessThanOrEqual() {
        EventEntity event = createEventEntityWithoutId();
        event.setAgeRestriction(16);
        repository.save(event);

        EventFilterRequestDTO filter = new EventFilterRequestDTO();
        filter.setAgeRestriction(18);

        Specification<EventEntity> spec = eventSpecificationFactory.findFilter(filter);
        List<EventEntity> result = repository.findAll(spec);

        assertFalse(result.isEmpty());
    }

    @Tag("integration")
    @Test
    void findFilter_shouldReturnListEmpty_whenAgeRestrictionGreaterThanAllowed() {
        EventEntity event = createEventEntityWithoutId();
        event.setAgeRestriction(21);
        repository.save(event);

        EventFilterRequestDTO filter = new EventFilterRequestDTO();
        filter.setAgeRestriction(18);

        Specification<EventEntity> spec = eventSpecificationFactory.findFilter(filter);
        List<EventEntity> result = repository.findAll(spec);

        assertTrue(result.isEmpty());
    }

    @Tag("integration")
    @Test
    void findFilter_shouldReturnListEmpty_whenAvailableTrueAndCapacityExceeded() {
        EventEntity event = createEventEntityWithoutId();
        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithoutId();
        TicketEntity ticketEntity = createTicketEntityPaymentApproved();

        holderRepository.save(ticketEntity.getHolderEntity());
        orderRepository.save(ticketEntity.getOrderEntity());
        paymentStatusRepository.save(ticketEntity.getPaymentStatusEntity());

        ticketEntity.setTicketTypeEntity(ticketTypeEntity);
        ticketTypeEntity.setTicketEntities(List.of(ticketEntity));
        ticketTypeEntity.setEventEntity(event);
        event.setCapacity(1);
        event.setTicketTypeEntities(List.of(ticketTypeEntity));

        repository.save(event);

        EventFilterRequestDTO filter = new EventFilterRequestDTO();
        filter.setAvailable(true);

        Specification<EventEntity> spec = eventSpecificationFactory.findFilter(filter);
        List<EventEntity> result = repository.findAll(spec);

        assertTrue(result.isEmpty());
    }

    @Tag("integration")
    @Test
    void findFilter_shouldReturnListEventEntity_whenAvailableIsFalseAndNotDeleted() {
        EventEntity event = createEventEntityWithoutId();
        repository.save(event);

        EventFilterRequestDTO filter = new EventFilterRequestDTO();
        filter.setAvailable(false);

        Specification<EventEntity> spec = eventSpecificationFactory.findFilter(filter);
        List<EventEntity> result = repository.findAll(spec);

        assertFalse(result.isEmpty());
    }
}