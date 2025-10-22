package com.tcc.api_ticket_sales.domain.exception;

public class HolderAlreadyHasTicketTypeException extends RuntimeException {
    public HolderAlreadyHasTicketTypeException(String holderName, String tickeTypetId) {

        super(String.format("O titular %s já possui o tipo de ingresso %s", holderName, tickeTypetId));
    }
}
