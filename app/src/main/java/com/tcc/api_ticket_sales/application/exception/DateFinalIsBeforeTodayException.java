package com.tcc.api_ticket_sales.application.exception;

public class DateFinalIsBeforeTodayException extends RuntimeException {
    public DateFinalIsBeforeTodayException() {
        super(ErrorMessages.DATE_FINAL_IS_BEFORE_TODAY);
    }
}
