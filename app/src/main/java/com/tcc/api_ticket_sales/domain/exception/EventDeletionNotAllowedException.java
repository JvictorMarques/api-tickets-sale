package com.tcc.api_ticket_sales.domain.exception;

public class EventDeletionNotAllowedException extends ConflictException {
    public EventDeletionNotAllowedException() {
        super("O evento não pode ser deletado pois possui ingresso(s) vendido(s).");
    }
}
