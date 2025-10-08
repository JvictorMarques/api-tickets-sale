package com.tcc.api_ticket_sales.domain.exception;

public class DateInvalidException extends RuntimeException {
    public DateInvalidException() {
      super(ErrorMessagesDomain.DATE_INVALID);
    }
}
