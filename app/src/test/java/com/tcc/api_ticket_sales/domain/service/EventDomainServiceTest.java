package com.tcc.api_ticket_sales.domain.service;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.exception.EventAgeRestrictionIncreaseNotAllowedException;
import com.tcc.api_ticket_sales.domain.exception.EventCapacityReductionNotAllowedException;
import com.tcc.api_ticket_sales.domain.exception.EventClosedException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithoutId;
import static com.tcc.api_ticket_sales.factory.TicketFactory.createListTicketEntityPaymentApproved;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeEntityWithId;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventDomainServiceTest {

    @InjectMocks
    private EventDomainService eventDomainService;

    @Tag("unit")
    @Test
    void update_shouldDoNothing_whenEventHasNoTicketTypes(){
        EventEntity event1 = createEventEntityWithoutId();
        EventEntity event2 = createEventEntityWithoutId();

        assertDoesNotThrow(() -> eventDomainService.updateEvent(event1, event2));
    }

    @Tag("unit")
    @Test
    void update_shouldThrowEventClosedException_whenEventIsDeleted(){
        EventEntity eventOld = createEventEntityWithoutId();
        eventOld.setDeletedAt(LocalDateTime.now());
        EventEntity eventNew = createEventEntityWithoutId();

        assertThrows(EventClosedException.class, () -> {
            eventDomainService.updateEvent(eventOld, eventNew);
        });
    }

    @Tag("unit")
    @Test
    void update_shouldThrowEventClosedException_whenEventEnd(){
        EventEntity eventOld = createEventEntityWithoutId();
        eventOld.setDateFinal(LocalDateTime.now());
        EventEntity eventNew = createEventEntityWithoutId();

        assertThrows(EventClosedException.class, () -> {
            eventDomainService.updateEvent(eventOld, eventNew);
        });
    }


    @Tag("unit")
    @Test
    void update_shouldThrowEventCapacityReductionNotAllowedException_whenEventCapacityReductionNotAllowed(){
        EventEntity eventOld = createEventEntityWithoutId();
        EventEntity eventNew = createEventEntityWithoutId();
        eventNew.setCapacity(1);

        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithId();
        eventOld.setTicketTypeEntities(List.of(ticketTypeEntity));

         assertThrows(EventCapacityReductionNotAllowedException.class, () ->{
           eventDomainService.updateEvent(eventOld, eventNew);
        });
    }

    @Tag("unit")
    @Test
    void update_shouldThrowEventAgeRestrictionIncreaseNotAllowedException_whenEventAgeRestrictionIncreaseNotAllowed(){
        EventEntity eventOld = createEventEntityWithoutId();
        EventEntity eventNew = createEventEntityWithoutId();


        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithId();
        ticketTypeEntity.setTicketEntities(createListTicketEntityPaymentApproved());

        eventOld.setTicketTypeEntities(List.of(ticketTypeEntity));
        eventNew.setCapacity(ticketTypeEntity.getCapacity());
        eventNew.setAgeRestriction(eventOld.getAgeRestriction() + 1);

        assertThrows(EventAgeRestrictionIncreaseNotAllowedException.class, () ->{
            eventDomainService.updateEvent(eventOld, eventNew);
        });
    }

    @Tag("unit")
    @Test
    void update_shouldUpdateTicketTypeEvent_whenTicketTypeEndDateIsAfterEventEndDate(){
        EventEntity eventOld = createEventEntityWithoutId();
        EventEntity eventNew = createEventEntityWithoutId();


        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithId();
        ticketTypeEntity.setDateFinal(eventNew.getDateFinal().plusDays(2));

        eventOld.setTicketTypeEntities(List.of(ticketTypeEntity));
        eventNew.setCapacity(ticketTypeEntity.getCapacity());
        eventNew.setAgeRestriction(eventOld.getAgeRestriction() + 1);

        assertDoesNotThrow(() -> eventDomainService.updateEvent(eventOld, eventNew));
        assertEquals(ticketTypeEntity.getDateFinal(), eventNew.getDateFinal());
    }


    @Tag("unit")
    @Test
    void update_shouldNoUpdateTicketTypeEvent_whenTicketTypeEndDateIsBeforeEventEndDate(){
        EventEntity eventOld = createEventEntityWithoutId();
        EventEntity eventNew = createEventEntityWithoutId();


        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithId();
        LocalDateTime dateFinal = eventNew.getDateFinal().minusDays(2);
        ticketTypeEntity.setDateFinal(dateFinal);

        eventOld.setTicketTypeEntities(List.of(ticketTypeEntity));
        eventNew.setCapacity(ticketTypeEntity.getCapacity());
        eventNew.setAgeRestriction(eventOld.getAgeRestriction() + 1);

        assertDoesNotThrow(() -> eventDomainService.updateEvent(eventOld, eventNew));
        assertEquals(ticketTypeEntity.getDateFinal(), dateFinal);
    }
}