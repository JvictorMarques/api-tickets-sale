package com.tcc.api_ticket_sales.domain.exception;

public class HolderAlreadyHasTicketException extends ConflictException {
    public HolderAlreadyHasTicketException(String holderName, String tickeTypetId) {

        super(String.format("O titular %s já possui ingresso para o evento", holderName, tickeTypetId));
    }
}
