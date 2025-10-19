package com.tcc.api_ticket_sales.domain.exception;

public class DateInvalidException extends BusinessException {
    public DateInvalidException() {
      super("Data inválida.");
    }
}
