package com.tcc.api_ticket_sales.application.exception;

import static com.tcc.api_ticket_sales.application.exception.ErrorMessagesApplication.EVENT_CLOSED;

public class EventClosedException extends RuntimeException {
    public EventClosedException() {
        super(EVENT_CLOSED);
    }
}
