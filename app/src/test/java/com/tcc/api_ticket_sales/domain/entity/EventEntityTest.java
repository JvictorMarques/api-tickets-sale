package com.tcc.api_ticket_sales.domain.entity;

import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import com.tcc.api_ticket_sales.domain.exception.DateInitialGreaterThanDateFinalException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventEntityTest {

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenNameIsNull(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        Exception exception = assertThrows(BusinessException.class, () -> {
            EventEntity.of(null, "Descrição", start, end, 100, 18, "São Paulo");
        });
        assertEquals("Nome do evento inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenNameIsEmpty(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        Exception exception = assertThrows(BusinessException.class, () -> {
            EventEntity.of("", "Descrição", start, end, 100, 18, "São Paulo");
        });
        assertEquals("Nome do evento inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenLocationIsNull(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        Exception exception = assertThrows(BusinessException.class, () -> {
            EventEntity.of(
                    "Evento Teste",
                    "Descrição",
                    start,
                    end,
                    100,
                    18,
                    null
            );
        });
        assertEquals("Local do evento inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenLocationIsEmpty(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        Exception exception = assertThrows(BusinessException.class, () -> {
            EventEntity.of(
                    "Evento Test",
                    "Descrição",
                    start,
                    end,
                    100,
                    18,
                    ""
            );
        });
        assertEquals("Local do evento inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenCapacityEqual0(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        Exception exception = assertThrows(BusinessException.class, () -> {
            EventEntity.of(
                    "Evento Test",
                    "Descrição",
                    start,
                    end,
                    0,
                    18,
                    "Local Teste"
            );
        });
        assertEquals("Capacidade do evento inválida", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenCapacityLessThan0(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        Exception exception = assertThrows(BusinessException.class, () -> {
            EventEntity.of(
                    "Evento Test",
                    "Descrição",
                    start,
                    end,
                    -1,
                    18,
                    "Local Teste"
            );
        });
        assertEquals("Capacidade do evento inválida", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenDateInitialNull(){
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        Exception exception = assertThrows(BusinessException.class, () -> {
            EventEntity.of(
                    "Evento Teste",
                    "Descrição",
                    null,
                    end,
                    100,
                    18,
                    "São Paulo"
            );
        });

        assertEquals("Data inicial do evento inválida", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenDateFinalNull(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);

        Exception exception = assertThrows(BusinessException.class, () -> {
            EventEntity.of(
                    "Evento Teste",
                    "Descrição",
                    start,
                    null,
                    100,
                    18,
                    "São Paulo"
            );
        });

        assertEquals("Data final do evento inválida", exception.getMessage());
    }


    @Test
    @Tag("unit")
    void of_shouldThrowDateInitialGreaterThanDateFinalException_WhenDateInitialGreaterThanDateFinal(){
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        assertThrows(DateInitialGreaterThanDateFinalException.class, () -> {
            EventEntity.of(
                    "Evento Teste",
                    "Descrição",
                    start,
                    end,
                    100,
                    18,
                    "São Paulo"
            );
        });
    }
}