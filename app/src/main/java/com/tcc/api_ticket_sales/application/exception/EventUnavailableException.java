package com.tcc.api_ticket_sales.application.exception;

public class EventUnavailableException extends RuntimeException {
    public EventUnavailableException() {
        super(ErrorMessages.EVENT_UNAVAILABLE);
    }
}
