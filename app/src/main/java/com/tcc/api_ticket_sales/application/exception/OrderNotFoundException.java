package com.tcc.api_ticket_sales.application.exception;

import jakarta.persistence.EntityNotFoundException;

public class OrderNotFoundException extends EntityNotFoundException {
    public OrderNotFoundException(String orderId) {
        super("Order " + orderId + " não encontrada.");
    }
}
