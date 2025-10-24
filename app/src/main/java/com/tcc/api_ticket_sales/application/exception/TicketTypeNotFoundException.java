package com.tcc.api_ticket_sales.application.exception;

import jakarta.persistence.EntityNotFoundException;

public class TicketTypeNotFoundException extends EntityNotFoundException {
    public TicketTypeNotFoundException(String id) {

        super(String.format("Tipo de ingresso %s não encontrado.", id));
    }
}
