package com.tcc.api_ticket_sales.application.exception;

import jakarta.persistence.EntityNotFoundException;

public class EventNotFoundException extends EntityNotFoundException {
    public EventNotFoundException(String id) {
        super(String.format("Evento %s não encontrado.", id));
    }
}
