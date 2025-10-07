package com.tcc.api_ticket_sales.domain.utils;

import com.tcc.api_ticket_sales.domain.exception.DateInitialGreaterThanDateFinalException;
import com.tcc.api_ticket_sales.domain.exception.DateInvalidException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CheckDate {
//    public static void checkFormat(String date) {
//        try {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate.parse(date, formatter);
//        } catch (Exception e) {
//            throw new DateInvalidException();
//        }
//    }

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
