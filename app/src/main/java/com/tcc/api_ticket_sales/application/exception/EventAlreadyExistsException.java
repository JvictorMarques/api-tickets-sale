package com.tcc.api_ticket_sales.application.exception;

import com.tcc.api_ticket_sales.domain.exception.ConflictException;

public class EventAlreadyExistsException extends ConflictException {
    public EventAlreadyExistsException() {
        super("Evento existente.");
    }
}
