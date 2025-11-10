package com.tcc.api_ticket_sales.domain.entity;

import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import com.tcc.api_ticket_sales.domain.exception.DateInitialGreaterThanDateFinalException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithId;
import static org.junit.jupiter.api.Assertions.*;

class TicketTypeEntityTest {

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenNameIsNull(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        EventEntity eventEntity = createEventEntityWithId();

        Exception exception = assertThrows(BusinessException.class, () -> {
            TicketTypeEntity.of(
                    null,
                    "description test",
                    BigDecimal.valueOf(9),
                    eventEntity,
                    100,
                    start,
                    end
            );
        });
        assertEquals("Nome do ingresso inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenNameIsEmpty(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        EventEntity eventEntity = createEventEntityWithId();

        Exception exception = assertThrows(BusinessException.class, () -> {
            TicketTypeEntity.of(
                    "",
                    "description test",
                    BigDecimal.valueOf(9),
                    eventEntity,
                    100,
                    start,
                    end
            );
        });
        assertEquals("Nome do ingresso inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenDateInitialNull(){
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        EventEntity eventEntity = createEventEntityWithId();

        Exception exception = assertThrows(BusinessException.class, () -> {
            TicketTypeEntity.of(
                    "Ticket test",
                    "description test",
                    BigDecimal.valueOf(9),
                    eventEntity,
                    100,
                    null,
                    end
            );
        });

        assertEquals("Data inicial do ingresso inválida", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenDateFinalNull(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        EventEntity eventEntity = createEventEntityWithId();

        Exception exception = assertThrows(BusinessException.class, () -> {
            TicketTypeEntity.of(
                    "Ticket test",
                    "description test",
                    BigDecimal.valueOf(9),
                    eventEntity,
                    100,
                    start,
                    null
            );
        });

        assertEquals("Data final do ingresso inválida", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowDateInitialGreaterThanDateFinalException_WhenDateInitialGreaterThanDateFinal(){
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        EventEntity eventEntity = createEventEntityWithId();

        assertThrows(DateInitialGreaterThanDateFinalException.class, () -> {
            TicketTypeEntity.of(
                    "Ticket test",
                    "description test",
                    BigDecimal.valueOf(9),
                    eventEntity,
                    100,
                    start,
                    end
            );
        });
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenCapacityEqual0(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        EventEntity eventEntity = createEventEntityWithId();

        Exception exception = assertThrows(BusinessException.class, () -> {
            TicketTypeEntity.of(
                    "Tickt test",
                    "description test",
                    BigDecimal.valueOf(9),
                    eventEntity,
                    0,
                    start,
                    end
            );
        });
        assertEquals("Capacidade do ingresso inválida", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenCapacityLessThan0(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        EventEntity eventEntity = createEventEntityWithId();

        Exception exception = assertThrows(BusinessException.class, () -> {
            TicketTypeEntity.of(
                    "Tickt test",
                    "description test",
                    BigDecimal.valueOf(9),
                    eventEntity,
                    -2,
                    start,
                    end
            );
        });
        assertEquals("Capacidade do ingresso inválida", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenPriceEqual0(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        EventEntity eventEntity = createEventEntityWithId();

        Exception exception = assertThrows(BusinessException.class, () -> {
            TicketTypeEntity.of(
                    "Ticket test",
                    "description test",
                    BigDecimal.valueOf(0),
                    eventEntity,
                    10,
                    start,
                    end
            );
        });
        assertEquals("Preço do ingresso inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenPriceLessThan0(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        EventEntity eventEntity = createEventEntityWithId();

        Exception exception = assertThrows(BusinessException.class, () -> {
            TicketTypeEntity.of(
                    "Tickt test",
                    "description test",
                    BigDecimal.valueOf(-1),
                    eventEntity,
                    10,
                    start,
                    end
            );
        });
        assertEquals("Preço do ingresso inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenPriceIsNull(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        EventEntity eventEntity = createEventEntityWithId();

        Exception exception = assertThrows(BusinessException.class, () -> {
            TicketTypeEntity.of(
                    "Tickt test",
                    "description test",
                    null,
                    eventEntity,
                    10,
                    start,
                    end
            );
        });
        assertEquals("Preço do ingresso inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenEventEntityNull(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        Exception exception = assertThrows(BusinessException.class, () -> {
            TicketTypeEntity.of(
                    "Tickt test",
                    "description test",
                    BigDecimal.valueOf(9),
                    null,
                    10,
                    start,
                    end
            );
        });
        assertEquals("Evento inválido para vincular o ingresso", exception.getMessage());
    }
}