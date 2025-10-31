package com.tcc.api_ticket_sales.domain.entity;

import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.tcc.api_ticket_sales.factory.OrderFactory.createOrderEntityWithId;
import static com.tcc.api_ticket_sales.factory.PaymentMethodFactory.createPaymentMethodEntity;
import static com.tcc.api_ticket_sales.factory.PaymentStatusFactory.createPaymentStatusEntity;
import static org.junit.jupiter.api.Assertions.*;


class PaymentEntityTest {
    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenAmountIsNull(){
        OrderEntity orderEntity = createOrderEntityWithId();
        PaymentMethodEntity paymentMethodEntity = createPaymentMethodEntity();
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();

        Exception exception = assertThrows(BusinessException.class, () -> {
            PaymentEntity.of(null, orderEntity, paymentMethodEntity, paymentStatusEntity);
        });

        assertEquals("O valor do pagamento inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenAmountIsZero(){
        OrderEntity orderEntity = createOrderEntityWithId();
        PaymentMethodEntity paymentMethodEntity = createPaymentMethodEntity();
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();

        Exception exception = assertThrows(BusinessException.class, () -> {
            PaymentEntity.of(BigDecimal.ZERO, orderEntity, paymentMethodEntity, paymentStatusEntity);
        });

        assertEquals("O valor do pagamento inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenOrderEntityIsNull(){
        PaymentMethodEntity paymentMethodEntity = createPaymentMethodEntity();
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();

        Exception exception = assertThrows(BusinessException.class, () -> {
            PaymentEntity.of(BigDecimal.valueOf(10), null, paymentMethodEntity, paymentStatusEntity);
        });

        assertEquals("Ordem inválida", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenPaymentMethodEntityIsNull(){
        OrderEntity orderEntity = createOrderEntityWithId();
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();

        Exception exception = assertThrows(BusinessException.class, () -> {
            PaymentEntity.of(BigDecimal.valueOf(10), orderEntity, null, paymentStatusEntity);
        });

        assertEquals("Método de pagamento inválido", exception.getMessage());
    }

    @Test
    @Tag("unit")
    void of_shouldThrowBusinessException_WhenPaymentStatusEntityIsNull(){
        OrderEntity orderEntity = createOrderEntityWithId();
        PaymentMethodEntity paymentMethodEntity = createPaymentMethodEntity();

        Exception exception = assertThrows(BusinessException.class, () -> {
            PaymentEntity.of(BigDecimal.valueOf(10), orderEntity, paymentMethodEntity, null);
        });

        assertEquals("Status de pagamento inválido", exception.getMessage());
    }
}