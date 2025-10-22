package com.tcc.api_ticket_sales.domain.exception;

public class TicketTypeDatesExceedsEventDateException extends ConflictException {
    public TicketTypeDatesExceedsEventDateException() {
        super(
                "A data de venda dos ingressos não pode ser após o evento."
        );
    }
}
