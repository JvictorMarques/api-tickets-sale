package com.tcc.api_ticket_sales.factory;

import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import org.instancio.Instancio;
import org.instancio.Select;

import java.util.List;

import static com.tcc.api_ticket_sales.factory.PaymentStatusFactory.createPaymentStatusEntityApproved;

public class TicketFactory {
    public static List<TicketEntity> createListTicketEntity(){
        return Instancio.ofList(TicketEntity.class).size(2).create();
    }

    public static List<TicketEntity> createListTicketEntityPaymentApproved(){
        return Instancio.ofList(TicketEntity.class).size(3)
                .set(Select.field(TicketEntity::getPaymentStatusEntity), createPaymentStatusEntityApproved())
                .set(Select.field(TicketEntity::getDeletedAt), null)
                .create();
    }

    public static TicketEntity createTicketEntity(){
        return Instancio.create(TicketEntity.class);
    }
}
