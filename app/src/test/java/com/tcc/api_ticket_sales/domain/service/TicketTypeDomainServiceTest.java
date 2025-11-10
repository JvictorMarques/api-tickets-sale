package com.tcc.api_ticket_sales.domain.service;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.exception.EventClosedException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeCapacityExceedsEventLimitException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeClosedException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeDatesExceedsEventDateException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithId;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeEntityWithoutId;
import static org.junit.jupiter.api.Assertions.*;

class TicketTypeDomainServiceTest {

    private final TicketTypeDomainService service = new TicketTypeDomainService();

    @Test
    @Tag("unit")
    void createTicketType_shouldThrowEventClosedException_WhenEventIsClosed() {
        EventEntity event = createEventEntityWithId();
        event.setDeletedAt(LocalDateTime.now());

        TicketTypeEntity ticketType = createTicketTypeEntityWithoutId();

        assertThrows(EventClosedException.class, () -> {
            service.createTicketType(event, new ArrayList<>(), ticketType);
        });
    }

    @Test
    @Tag("unit")
    void createTicketType_shouldThrowTicketTypeCapacityExceedsEventLimitException_WhenCapacityExceeded() {
        EventEntity event = createEventEntityWithId();
        event.setCapacity(100);

        List<TicketTypeEntity> existingTypes = new ArrayList<>();
        TicketTypeEntity type1 = createTicketTypeEntityWithoutId();
        type1.setCapacity(60);
        existingTypes.add(type1);

        TicketTypeEntity newType = createTicketTypeEntityWithoutId();
        newType.setCapacity(50);

        Exception exception = assertThrows(TicketTypeCapacityExceedsEventLimitException.class, () -> {
            service.createTicketType(event, existingTypes, newType);
        });

        assertTrue(exception.getMessage().contains("40"));
    }

    @Test
    @Tag("unit")
    void createTicketType_shouldThrowTicketTypeDatesExceedsEventDateException_WhenDateExceeds() {
        EventEntity event = createEventEntityWithId();

        TicketTypeEntity ticketType = createTicketTypeEntityWithoutId();
        ticketType.setDateInitial(event.getDateFinal().plusDays(3));
        ticketType.setCapacity(50);

        assertThrows(TicketTypeDatesExceedsEventDateException.class, () -> {
            service.createTicketType(event, new ArrayList<>(), ticketType);
        });
    }

    @Test
    @Tag("unit")
    void validateTicketTypeSale_shouldThrowTicketTypeClosedException_WhenTypeIsDeleted() {
        TicketTypeEntity type = createTicketTypeEntityWithoutId();
        type.setDeletedAt(LocalDateTime.now());

        assertThrows(TicketTypeClosedException.class, () -> {
            service.validateTicketTypeSale(type);
        });
    }

    @Test
    @Tag("unit")
    void validateTicketTypeSale_shouldThrowTicketTypeClosedException_WhenDateFinalIsPast() {
        TicketTypeEntity type = createTicketTypeEntityWithoutId();
        type.setDateFinal(LocalDateTime.now().minusDays(1));

        assertThrows(TicketTypeClosedException.class, () -> {
            service.validateTicketTypeSale(type);
        });
    }

    @Test
    @Tag("unit")
    void validateTicketTypeSale_shouldThrowEventClosedException_WhenEventIsClosed() {
        EventEntity event = createEventEntityWithId();
        event.setDeletedAt(LocalDateTime.now());

        TicketTypeEntity type = createTicketTypeEntityWithoutId();
        type.setEventEntity(event);
        type.setDateFinal(LocalDateTime.now().plusDays(1));

        assertThrows(EventClosedException.class, () -> {
            service.validateTicketTypeSale(type);
        });
    }
}