package com.tcc.api_ticket_sales.factory;

import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentMethodEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static com.tcc.api_ticket_sales.factory.OrderFactory.createOrderEntityWithId;
import static com.tcc.api_ticket_sales.factory.PaymentMethodFactory.createPaymentMethodEntity;
import static com.tcc.api_ticket_sales.factory.PaymentStatusFactory.createPaymentStatusEntity;

public class PaymentFactory {

    public static PaymentEntity createPaymentEntityWithId(){
        OrderEntity order = createOrderEntityWithId();
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();
        PaymentMethodEntity paymentMethodEntity = createPaymentMethodEntity();

        PaymentEntity paymentEntity = PaymentEntity.of(
                BigDecimal.valueOf(100),
                order,
                paymentMethodEntity,
                paymentStatusEntity
        );

        paymentEntity.setId(UUID.randomUUID());

        return paymentEntity;
    }
}
