package com.tcc.api_ticket_sales.application.exception;

import jakarta.persistence.EntityNotFoundException;

public class PaymentMethodNotFoundException extends EntityNotFoundException {
    public PaymentMethodNotFoundException() {
        super("Método de pagamento não encontrado.");
    }
}
