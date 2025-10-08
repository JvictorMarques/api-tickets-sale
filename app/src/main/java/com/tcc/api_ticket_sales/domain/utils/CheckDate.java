package com.tcc.api_ticket_sales.domain.utils;

import com.tcc.api_ticket_sales.domain.exception.DateInitialGreaterThanDateFinalException;
import com.tcc.api_ticket_sales.domain.exception.DateInvalidException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CheckDate {

    private CheckDate() {
        throw new IllegalStateException("Utility class");
    }

    public static void checkDateInitialGreaterThanDateFinal(LocalDate dateInitial, LocalDate dateFinal) {
        if(dateInitial == null || dateFinal == null){
            throw new DateInvalidException();
        }

        if(dateInitial.isAfter(dateFinal)){
            throw new DateInitialGreaterThanDateFinalException();
        }
    }

    public static void checkDateInitialGreaterThanDateFinal(LocalDateTime dateInitial, LocalDateTime dateFinal) {
        if(dateInitial == null || dateFinal == null){
            throw new DateInvalidException();
        }

        if(dateInitial.isAfter(dateFinal)){
            throw new DateInitialGreaterThanDateFinalException();
        }
    }
}
