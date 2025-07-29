package com.tcc.api_ticket_sales.application.exception;

public class DateInitialGreaterThanDateFinalException extends RuntimeException {
  public DateInitialGreaterThanDateFinalException() {
    super(ErrorMessages.DATE_INITIAL_GREATER_THAN_DATE_FINAL);
  }
}
