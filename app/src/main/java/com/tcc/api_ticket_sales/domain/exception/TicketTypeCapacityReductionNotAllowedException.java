package com.tcc.api_ticket_sales.domain.exception;

public class TicketTypeCapacityReductionNotAllowedException extends ConflictException {
    public TicketTypeCapacityReductionNotAllowedException(long quantityPurchased) {
        super(String.format(
                "Capacidade inválida para atualização: existem %d ingressos já vendidos.",
                quantityPurchased));
    }
}
