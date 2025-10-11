package com.tcc.api_ticket_sales.domain.exception;

public class DateInitialGreaterThanDateFinalException extends BusinessException {
  public DateInitialGreaterThanDateFinalException() {
    super("Data inicial maior que data final.");
  }
}
