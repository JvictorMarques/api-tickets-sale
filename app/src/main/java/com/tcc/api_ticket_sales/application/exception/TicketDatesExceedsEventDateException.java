package com.tcc.api_ticket_sales.application.exception;

import com.tcc.api_ticket_sales.domain.exception.ConflictException;

public class TicketDatesExceedsEventDateException extends ConflictException {
    public TicketDatesExceedsEventDateException() {
        super(
                "A data de venda dos ingressos não pode ser após o evento."
        );
    }
}
