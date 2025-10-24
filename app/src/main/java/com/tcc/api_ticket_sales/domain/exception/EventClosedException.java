package com.tcc.api_ticket_sales.domain.exception;

public class EventClosedException extends ConflictException {
    public EventClosedException() {
        super("Evento encerrado.");
    }
}
