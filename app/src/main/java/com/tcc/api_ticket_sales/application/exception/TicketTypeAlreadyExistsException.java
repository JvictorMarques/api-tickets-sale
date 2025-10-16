package com.tcc.api_ticket_sales.application.exception;

import com.tcc.api_ticket_sales.domain.exception.ConflictException;

public class TicketTypeAlreadyExistsException extends ConflictException {
    public TicketTypeAlreadyExistsException() {
        super("Tipo de ingresso duplicado: este evento já o tipo de ingresso");
    }
}
