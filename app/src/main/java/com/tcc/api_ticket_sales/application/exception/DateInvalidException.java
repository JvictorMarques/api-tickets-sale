package com.tcc.api_ticket_sales.application.exception;

public class DateInvalidException extends RuntimeException {
    public DateInvalidException() {
      super(ErrorMessages.DATE_INVALID);
    }
}
