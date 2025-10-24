package com.tcc.api_ticket_sales.factory;

import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;

import java.util.UUID;

public class PaymentStatusFactory {
    public static PaymentStatusEntity createPaymentStatusEntity(){
        PaymentStatusEntity paymentStatusEntity = new PaymentStatusEntity("teste");
        paymentStatusEntity.setId(UUID.randomUUID());

        return paymentStatusEntity;
    }
}
