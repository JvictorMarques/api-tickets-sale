package com.tcc.api_ticket_sales.factory;
import com.tcc.api_ticket_sales.domain.entity.PaymentMethodEntity;

import java.util.UUID;

public class PaymentMethodFactory {
    public static PaymentMethodEntity createPaymentMethodEntity(){
        PaymentMethodEntity paymentMethodEntity = new PaymentMethodEntity("teste");
        paymentMethodEntity.setId(UUID.randomUUID());

        return paymentMethodEntity;
    }
}
