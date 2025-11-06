package com.tcc.api_ticket_sales.domain.service;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.exception.EventClosedException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeCapacityReductionNotAllowedException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeCapacityExceedsEventLimitException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeClosedException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeDatesExceedsEventDateException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeDeletionNotAllowedException;
import com.tcc.api_ticket_sales.domain.model.TicketBuyModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithId;
import static com.tcc.api_ticket_sales.factory.TicketFactory.createListTicketEntityPaymentApproved;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeEntityWithId;
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

    // teste new

    @Test
    @Tag("unit")
    void updateTicketType_shouldThrowTicketTypeClosedException_WhenTicketTypeIsDeleted() {
        TicketTypeEntity ticketType = createTicketTypeEntityWithoutId();
        ticketType.setDeletedAt(LocalDateTime.now());

        assertThrows(TicketTypeClosedException.class, () -> {
            service.updateTicketType(ticketType);
        });
    }

    @Test
    @Tag("unit")
    void updateTicketType_shouldThrowEventClosedException_WhenEventIsClosed() {
        TicketTypeEntity ticketType = createTicketTypeEntityWithoutId();
        EventEntity event = createEventEntityWithId();
        event.setDeletedAt(LocalDateTime.now());

        ticketType.setEventEntity(event);

        assertThrows(EventClosedException.class, () -> {
            service.updateTicketType(ticketType);
        });
    }

    @Test
    @Tag("unit")
    void updateTicketType_shouldThrowTicketInvalidQuantityUpdateException_WhenInvalidQuantity() {
        EventEntity event = createEventEntityWithId();

        TicketTypeEntity ticketType = createTicketTypeEntityWithoutId();
        ticketType.setCapacity(2);
        ticketType.setEventEntity(event);

        ticketType.setTicketEntities(createListTicketEntityPaymentApproved());

        assertThrows(TicketTypeCapacityReductionNotAllowedException.class, () -> {
            service.updateTicketType(ticketType);
        });
    }

    @Test
    @Tag("unit")
    void updateTicketType_shouldThrowTicketTypeCapacityExceedsEventLimitException_WhenCapacityExceeded() {
        EventEntity event = createEventEntityWithId();
        event.setCapacity(100);

        TicketTypeEntity type1 = createTicketTypeEntityWithId();
        type1.setCapacity(60);

        event.setTicketTypeEntities(List.of(type1));

        TicketTypeEntity newType = createTicketTypeEntityWithId();
        newType.setCapacity(50);
        newType.setEventEntity(event);
        newType.setTicketEntities(List.of());

        Exception exception = assertThrows(TicketTypeCapacityExceedsEventLimitException.class, () -> {
            service.updateTicketType(newType);
        });

        assertTrue(exception.getMessage().contains("40"));
    }

    @Test
    @Tag("unit")
    void updateTicketType_shouldThrowTicketTypeDatesExceedsEventDateException_WhenDateExceeds() {
        EventEntity event = createEventEntityWithId();
        event.setTicketTypeEntities(List.of());

        TicketTypeEntity ticketType = createTicketTypeEntityWithoutId();
        ticketType.setDateInitial(event.getDateFinal().plusDays(3));
        ticketType.setCapacity(50);
        ticketType.setTicketEntities(List.of());
        ticketType.setEventEntity(event);

        assertThrows(TicketTypeDatesExceedsEventDateException.class, () -> {
            service.updateTicketType(ticketType);
        });
    }

    @Test
    @Tag("unit")
    void updateTicketType_shouldReturnTicketType_WhenSuccess() {
        EventEntity event = createEventEntityWithId();
        event.setTicketTypeEntities(List.of());

        TicketTypeEntity ticketType = createTicketTypeEntityWithId();
        ticketType.setDateInitial(event.getDateFinal().minusDays(2));
        ticketType.setDateFinal(event.getDateFinal().minusDays(1));
        ticketType.setCapacity(50);
        ticketType.setTicketEntities(List.of());
        ticketType.setEventEntity(event);


        TicketTypeEntity response = service.updateTicketType(ticketType);
        assertEquals(response.getId(), ticketType.getId());
    }

    @Test
    @Tag("unit")
    void calculateTotalPrice_shouldReturnZero_whenListIsEmpty() {
        BigDecimal result = service.calculateTotalPrice(List.of());
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @Tag("unit")
    void calculateTotalPrice_shouldReturnCorrectTotal_whenListHasMultipleTickets() {
        // Arrange
        TicketTypeEntity type1 = createTicketTypeEntityWithoutId();
        type1.setPrice(BigDecimal.valueOf(100));

        TicketTypeEntity type2 = createTicketTypeEntityWithoutId();
        type2.setPrice(BigDecimal.valueOf(50));

        List<TicketBuyModel> tickets = List.of(
                new TicketBuyModel(type1, 2),
                new TicketBuyModel(type2, 3)
        );

        // Expected: (100 * 2) + (50 * 3) = 350
        BigDecimal expected = BigDecimal.valueOf(350);

        // Act
        BigDecimal result = service.calculateTotalPrice(tickets);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @Tag("unit")
    void deleteTicketType_shouldThrowTicketTypeDeletionNotAllowedException_whenTicketHasPurchases() {
        // Arrange
        TicketTypeEntity ticketType = createTicketTypeEntityWithoutId();
        ticketType.setTicketEntities(createListTicketEntityPaymentApproved());

        // Act & Assert
        assertThrows(TicketTypeDeletionNotAllowedException.class, () -> {
            service.deleteTicketType(ticketType);
        });
    }

    @Test
    @Tag("unit")
    void deleteTicketType_shouldSetDeletedAtAndReturn_whenNoTicketsPurchased() {
        // Arrange
        TicketTypeEntity ticketType = createTicketTypeEntityWithoutId();
        ticketType.setTicketEntities(List.of());

        // Act
        TicketTypeEntity result = service.deleteTicketType(ticketType);

        // Assert
        assertNotNull(result.getDeletedAt());
        assertTrue(result.getDeletedAt().isBefore(LocalDateTime.now()) ||
                result.getDeletedAt().isEqual(LocalDateTime.now()));
    }
}