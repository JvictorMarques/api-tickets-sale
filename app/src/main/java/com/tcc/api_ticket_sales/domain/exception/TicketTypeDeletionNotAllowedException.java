package com.tcc.api_ticket_sales.domain.exception;

public class TicketTypeDeletionNotAllowedException extends  ConflictException{
    public TicketTypeDeletionNotAllowedException() {
        super("Tipo de ingresso não pode ser deletado pois possui ingresso(s) vendido(s).");
    }
}
