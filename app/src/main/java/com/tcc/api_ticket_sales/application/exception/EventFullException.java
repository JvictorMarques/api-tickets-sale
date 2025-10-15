package com.tcc.api_ticket_sales.application.exception;

import com.tcc.api_ticket_sales.domain.exception.ConflictException;

public class EventFullException extends ConflictException {
    public EventFullException() {
        super("Evento com capacidade esgotada.");
    }
}
