package com.tcc.api_ticket_sales.application.exception;

import jakarta.persistence.EntityNotFoundException;

public class PaymentStatusNotFoundException extends EntityNotFoundException {
    public PaymentStatusNotFoundException() {

        super("Status do pagamento não encontrado.");
    }
}
