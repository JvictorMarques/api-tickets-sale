package com.tcc.api_ticket_sales.domain.exception;

public class DateInitialGreaterThanDateFinalException extends RuntimeException {
  public DateInitialGreaterThanDateFinalException() {
    super(ErrorMessagesDomain.DATE_INITIAL_GREATER_THAN_DATE_FINAL);
  }
}
