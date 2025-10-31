package com.tcc.api_ticket_sales.domain.exception;

public class TicketTypeClosedException extends ConflictException {
    public TicketTypeClosedException() {
        super("Tipo de ingresso encerrado.");
    }
}
