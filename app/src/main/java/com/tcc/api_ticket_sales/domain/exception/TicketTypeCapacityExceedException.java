package com.tcc.api_ticket_sales.domain.exception;

public class TicketTypeCapacityExceedException extends ConflictException {
    public TicketTypeCapacityExceedException() {
        super("A quantidade de ingressos da compra excede a quantidade disponível.");
    }
}
