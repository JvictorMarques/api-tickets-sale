package com.tcc.api_ticket_sales.application.helpers;

import com.tcc.api_ticket_sales.application.exception.DateFinalIsBeforeTodayException;
import com.tcc.api_ticket_sales.application.exception.DateInitialGreaterThanDateFinalException;
import com.tcc.api_ticket_sales.application.exception.DateInvalidException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CheckDate {
    public static void checkFormat(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(date, formatter);
        } catch (Exception e) {
            throw new DateInvalidException();
        }
    }

    public static void checkDateInitialGreaterThanDateFinal(LocalDate dateInitial, LocalDate dateFinal) {
        if(dateInitial.isAfter(dateFinal)){
            throw new DateInitialGreaterThanDateFinalException();
        }
    }

    public static void checkIntervalDateInvalid(LocalDate dateInitial, LocalDate dateFinal, boolean checkAllNull) {
        if(dateInitial != null && dateFinal == null){
            throw new DateInvalidException();
        }
        if(dateInitial == null && dateFinal != null){
            throw new DateInvalidException();
        }

        if(checkAllNull){
            if(dateInitial == null && dateFinal == null){
                throw new DateInvalidException();
            }
        }
    }

    public static void checkDateFinalIsBeforeToday(LocalDate dateFinal) {
        LocalDate today = LocalDate.now();
        if(today.isAfter(dateFinal)){
            throw new DateFinalIsBeforeTodayException();
        }
    }
}
