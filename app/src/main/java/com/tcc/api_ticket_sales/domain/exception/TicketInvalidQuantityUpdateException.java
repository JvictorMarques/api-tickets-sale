package com.tcc.api_ticket_sales.domain.exception;

public class TicketInvalidQuantityUpdateException extends ConflictException {
    public TicketInvalidQuantityUpdateException(long quantityPurchased) {
        super(String.format(
                "Quantidade inválida para atualização: existem %d ingressos já vendidos.",
                quantityPurchased));
    }
}
