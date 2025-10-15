package com.tcc.api_ticket_sales.application.exception;

import com.tcc.api_ticket_sales.domain.exception.ConflictException;

public class TicketAlreadyExistsException extends ConflictException {
    public TicketAlreadyExistsException() {
        super("Ingresso duplicado: este evento já o ingresso");
    }
}
