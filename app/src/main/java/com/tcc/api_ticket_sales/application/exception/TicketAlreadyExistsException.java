package com.tcc.api_ticket_sales.application.exception;

import static com.tcc.api_ticket_sales.application.exception.ErrorMessagesApplication.TICKET_ALREADY_EXISTS;

public class TicketAlreadyExistsException extends RuntimeException {
    public TicketAlreadyExistsException() {
        super(TICKET_ALREADY_EXISTS);
    }
}
