package com.tcc.api_ticket_sales.domain.entity;

import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.tcc.api_ticket_sales.factory.HolderFactory.createHolderEntityWithId;
import static com.tcc.api_ticket_sales.factory.OrderFactory.createOrderEntityWithId;
import static com.tcc.api_ticket_sales.factory.PaymentStatusFactory.createPaymentStatusEntity;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeEntityWithId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class TicketEntityTest {

    @Test
    @Tag("unit")
    public void of_ShouldThrowBusinessException_WhenTypeTicketEntityIsNull(){
        OrderEntity orderEntity = createOrderEntityWithId();
        HolderEntity holderEntity = createHolderEntityWithId();
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();

        Exception exception = assertThrows(BusinessException.class, () -> {
            new TicketEntity(
                    null,
                    holderEntity,
                    orderEntity,
                    paymentStatusEntity
            );
        });
        assertEquals("O tipo do ingresso não pode ser nulo", exception.getMessage());
    }

    @Test
    @Tag("unit")
    public void of_ShouldThrowBusinessException_WhenHolderEntityIsNull(){
        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithId();
        OrderEntity orderEntity = createOrderEntityWithId();
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();

        Exception exception = assertThrows(BusinessException.class, () -> {
            new TicketEntity(
                    ticketTypeEntity,
                    null,
                    orderEntity,
                    paymentStatusEntity
            );
        });
        assertEquals("O titular do ingresso não pode ser nulo", exception.getMessage());
    }

    @Test
    @Tag("unit")
    public void of_ShouldThrowBusinessException_WhenOrderEntityIsNull(){
        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithId();
        HolderEntity holderEntity = createHolderEntityWithId();
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();

        Exception exception = assertThrows(BusinessException.class, () -> {
            new TicketEntity(
                    ticketTypeEntity,
                    holderEntity,
                    null,
                    paymentStatusEntity
            );
        });
        assertEquals("A ordem do ingresso não pode ser nula", exception.getMessage());
    }

    @Test
    @Tag("unit")
    public void of_ShouldThrowBusinessException_WhenPaymentStatusEntityIsNull(){
        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithId();
        OrderEntity orderEntity = createOrderEntityWithId();
        HolderEntity holderEntity = createHolderEntityWithId();

        Exception exception = assertThrows(BusinessException.class, () -> {
            new TicketEntity(
                    ticketTypeEntity,
                    holderEntity,
                    orderEntity,
                    null
            );
        });
        assertEquals("O status de pagamento do ingresso não pode ser nulo", exception.getMessage());
    }
}