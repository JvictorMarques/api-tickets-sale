package com.tcc.api_ticket_sales.application.exception;

import com.tcc.api_ticket_sales.domain.exception.ConflictException;

public class EventClosedException extends ConflictException {
    public EventClosedException() {
        super("Evento encerrado.");
    }
}
