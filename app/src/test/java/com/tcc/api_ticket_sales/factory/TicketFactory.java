package com.tcc.api_ticket_sales.factory;

import com.tcc.api_ticket_sales.domain.entity.HolderEntity;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import org.instancio.Instancio;

import java.util.List;

import static com.tcc.api_ticket_sales.factory.PaymentStatusFactory.createPaymentStatusEntityApproved;
import static org.instancio.Select.field;

public class TicketFactory {
    public static List<TicketEntity> createListTicketEntity(){
        return Instancio.ofList(TicketEntity.class).size(2).create();
    }

    public static List<TicketEntity> createListTicketEntityPaymentApproved(){
        return Instancio.ofList(TicketEntity.class).size(3)
                .set(field(TicketEntity::getPaymentStatusEntity), createPaymentStatusEntityApproved())
                .set(field(TicketEntity::getDeletedAt), null)
                .create();
    }

    public static TicketEntity createTicketEntityPaymentApproved(){
        HolderEntity holder = Instancio.of(HolderEntity.class)
                .set(field(HolderEntity::getId), null)
                .set(field(HolderEntity::getTicketEntities), null)
                .create();

        OrderEntity order = Instancio.of(OrderEntity.class)
                .set(field(OrderEntity::getId), null)
                .set(field(OrderEntity::getPaymentEntities), null)
                .set(field(OrderEntity::getTicketEntities), null)
                .create();

        PaymentStatusEntity paymentStatusEntity = Instancio.of(PaymentStatusEntity.class)
                .set(field(PaymentStatusEntity::getId), null)
                .set(field(PaymentStatusEntity::getDescription), "approved")
                .create();

        return Instancio.of(TicketEntity.class)
                .set(field(TicketEntity::getId), null)
                .set(field(TicketEntity::getDeletedAt), null)
                .set(field(TicketEntity::getPaymentStatusEntity), paymentStatusEntity)
                .set(field(TicketEntity::getHolderEntity),holder)
                .set(field(TicketEntity::getOrderEntity),order)
                .create();
    }
}
