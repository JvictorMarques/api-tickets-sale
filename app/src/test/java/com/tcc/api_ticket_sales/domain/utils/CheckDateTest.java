package com.tcc.api_ticket_sales.domain.utils;

import com.tcc.api_ticket_sales.domain.exception.DateInitialGreaterThanDateFinalException;
import com.tcc.api_ticket_sales.domain.exception.DateInvalidException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class CheckDateTest {

    @Test
    public void checkDateInitialGreaterThanDateFinal_shouldNotThrowException_whenInitialBeforeFinal(){
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(1);

        assertDoesNotThrow(() ->
                CheckDate.checkDateInitialGreaterThanDateFinal(start, end)
        );
    }

    @Test
    public void checkDateInitialGreaterThanDateFinal_shouldNotThrowException_whenInitialEqualFinal(){
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now();

        assertDoesNotThrow(() ->
                CheckDate.checkDateInitialGreaterThanDateFinal(start, end)
        );
    }

    @Test
    public void checkDateInitialGreaterThanDateFinal_shouldThrowException_whenInitialAfterFinal(){
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now();

        assertThrows(DateInitialGreaterThanDateFinalException.class, () ->
                CheckDate.checkDateInitialGreaterThanDateFinal(start, end)
        );
    }

    @Test
    public void checkDateInitialGreaterThanDateFinal_shouldThrowException_whenInitialNull(){
        LocalDate start = null;
        LocalDate end = LocalDate.now();

        assertThrows(DateInvalidException.class, () ->
                CheckDate.checkDateInitialGreaterThanDateFinal(start, end)
        );
    }

    @Test
    public void checkDateInitialGreaterThanDateFinal_shouldThrowException_whenFinalNull(){
        LocalDate start = LocalDate.now();
        LocalDate end = null;

        assertThrows(DateInvalidException.class, () ->
                CheckDate.checkDateInitialGreaterThanDateFinal(start, end)
        );
    }

    @Test
    public void checkDateInitialGreaterThanDateFinal_shouldThrowException_whenInitialFinalNull(){
        LocalDate start = null;
        LocalDate end = null;

        assertThrows(DateInvalidException.class, () ->
                CheckDate.checkDateInitialGreaterThanDateFinal(start, end)
        );
    }

    @Test
    public void checkDateInitialGreaterThanDateFinal_LocalDateTime_shouldNotThrowException_whenInitialBeforeFinal(){
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        assertDoesNotThrow(() ->
                CheckDate.checkDateInitialGreaterThanDateFinal(start, end)
        );
    }

    @Test
    public void checkDateInitialGreaterThanDateFinal_LocalDateTime_shouldNotThrowException_whenInitialEqualFinal(){
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();

        assertDoesNotThrow(() ->
                CheckDate.checkDateInitialGreaterThanDateFinal(start, end)
        );
    }

    @Test
    public void checkDateInitialGreaterThanDateFinal_LocalDateTime_shouldThrowException_whenInitialAfterFinal(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now();

        assertThrows(DateInitialGreaterThanDateFinalException.class, () ->
                CheckDate.checkDateInitialGreaterThanDateFinal(start, end)
        );
    }

    @Test
    public void checkDateInitialGreaterThanDateFinal_LocalDateTime_shouldThrowException_whenInitialNull(){
        LocalDateTime start = null;
        LocalDateTime end = LocalDateTime.now();

        assertThrows(DateInvalidException.class, () ->
                CheckDate.checkDateInitialGreaterThanDateFinal(start, end)
        );
    }

    @Test
    public void checkDateInitialGreaterThanDateFinal_LocalDateTime_shouldThrowException_whenFinalNull(){
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = null;

        assertThrows(DateInvalidException.class, () ->
                CheckDate.checkDateInitialGreaterThanDateFinal(start, end)
        );
    }

    @Test
    public void checkDateInitialGreaterThanDateFinal_LocalDateTime_shouldThrowException_whenInitialFinalNull(){
        LocalDateTime start = null;
        LocalDateTime end = null;

        assertThrows(DateInvalidException.class, () ->
                CheckDate.checkDateInitialGreaterThanDateFinal(start, end)
        );
    }
}