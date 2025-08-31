package com.tcc.api_ticket_sales.application.helpers;

import com.tcc.api_ticket_sales.application.exception.DateFinalIsBeforeTodayException;
import com.tcc.api_ticket_sales.application.exception.DateInitialGreaterThanDateFinalException;
import com.tcc.api_ticket_sales.application.exception.DateInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CheckDateTest {

    @Test
    @Tag("unit")
    @DisplayName("Should return DateInvalidException because date's invalid format")
    void shouldReturnExceptionFormatDateInvalid(){
        assertThrows(DateInvalidException.class, () -> {
            CheckDate.checkFormat("2024-52-12");
        });
    }

    @Test
    @Tag("unit")
    @DisplayName("Should execute without exception because date's valid format")
    void shouldExecuteWithoutExceptionDateValid(){
        assertDoesNotThrow(() -> {
            CheckDate.checkFormat("2024-02-12");
        });
    }

    @Test
    @Tag("unit")
    @DisplayName("Should return DateInvalidException because final date is null")
    void shouldReturnExceptionDateFinalIsNull(){
        assertThrows(DateInvalidException.class, () -> {
            CheckDate.checkIntervalDateInvalid(LocalDate.now(), null, true);
        });
    }

    @Test
    @Tag("unit")
    @DisplayName("Should return DateInvalidException because initial date is null")
    void shouldReturnExceptionDateInitialIsNull(){
        assertThrows(DateInvalidException.class, () -> {
            CheckDate.checkIntervalDateInvalid(null, LocalDate.now(), true);
        });
    }

    @Test
    @Tag("unit")
    @DisplayName("Should execute without exception because dates are not null")
    void shouldExecuteWithoutExceptionDateAreNotNull(){
        assertDoesNotThrow(() -> {
            CheckDate.checkIntervalDateInvalid(LocalDate.now(), LocalDate.now(), true);
        });
    }

    @Test
    @Tag("unit")
    @DisplayName("Should return DateInvalidException because dates are null")
    void shouldReturnExceptionDatesAreNull(){
        assertThrows(DateInvalidException.class, () -> {
            CheckDate.checkIntervalDateInvalid(null, null, true);
        });
    }

    @Test
    @Tag("unit")
    @DisplayName("Should execute without exception because dates is null but checked is false")
    void shouldExecuteWithoutExceptionDateAreNullNoChecked(){
        assertDoesNotThrow(() -> {
            CheckDate.checkIntervalDateInvalid(null, null, false);
        });
    }

    @Test
    @Tag("unit")
    @DisplayName("Should return DateInitialGreaterThanDateFinalException")
    void shouldReturnDateInitialGreaterThanDateFinalException(){
        assertThrows(DateInitialGreaterThanDateFinalException.class, () -> {
            CheckDate.checkDateInitialGreaterThanDateFinal(LocalDate.now().plusMonths(1), LocalDate.now());
        });
    }

    @Test
    @Tag("unit")
    @DisplayName("Should execute without exception because date initial before date final")
    void shouldExecuteWithoutExceptionDateInitialBeforeFinal(){
        assertDoesNotThrow(() -> {
            CheckDate.checkDateInitialGreaterThanDateFinal(LocalDate.now(), LocalDate.now().plusMonths(1));
        });
    }

    @Test
    @Tag("unit")
    @DisplayName("Should return DateFinalIsBeforeTodayException")
    void shouldReturnDateFinalIsBeforeTodayException(){
        assertThrows(DateFinalIsBeforeTodayException.class, () -> {
            CheckDate.checkDateFinalIsBeforeToday(LocalDate.now().minusDays(1));
        });
    }

    @Test
    @Tag("unit")
    @DisplayName("Should execute without exception because date final greater than today")
    void shouldExecuteWithoutExceptionDateFinalGreaterThanToday(){
        assertDoesNotThrow(() -> {
            CheckDate.checkDateFinalIsBeforeToday(LocalDate.now().plusDays(1));
        });
    }

    @Test
    @Tag("unit")
    @DisplayName("Should execute without exception because date final equals today date")
    void shouldExecuteWithoutExceptionDateFinalEqualsToday(){
        assertDoesNotThrow(() -> {
            CheckDate.checkDateFinalIsBeforeToday(LocalDate.now());
        });
    }
}
