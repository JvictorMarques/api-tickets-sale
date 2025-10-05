package com.tcc.api_ticket_sales.application.exception;

import static com.tcc.api_ticket_sales.application.exception.ErrorMessages.EVENT_ALREADY_EXISTS;

public class EventAlreadyExistsException extends RuntimeException {
    public EventAlreadyExistsException() {
        super(EVENT_ALREADY_EXISTS);
    }
}
