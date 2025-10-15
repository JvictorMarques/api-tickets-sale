package com.tcc.api_ticket_sales.application.exception;

import com.tcc.api_ticket_sales.domain.exception.ConflictException;

public class TicketCapacityExceedsEventLimitException extends ConflictException {
    public TicketCapacityExceedsEventLimitException(int capacity) {
        super(String.format(
                "Capacidade insuficiente: apenas %d vagas restantes no evento para novos ingressos.",
                capacity
        ));
    }
}
