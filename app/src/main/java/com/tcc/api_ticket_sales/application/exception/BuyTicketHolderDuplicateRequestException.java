package com.tcc.api_ticket_sales.application.exception;

import com.tcc.api_ticket_sales.domain.exception.BusinessException;

public class BuyTicketHolderDuplicateRequestException extends BusinessException {
    public BuyTicketHolderDuplicateRequestException(String holderName) {

        super(String.format("Não é possível comprar mais de um ingresso para o titular: %s.",  holderName));
    }
}
