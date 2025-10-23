package com.tcc.api_ticket_sales.domain.service;

import com.tcc.api_ticket_sales.domain.entity.HolderEntity;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.enums.PaymentStatusEnum;
import com.tcc.api_ticket_sales.domain.exception.HolderAlreadyHasTicketException;
import com.tcc.api_ticket_sales.domain.exception.InvalidAgeForEventException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class TicketDomainService {

    public TicketEntity createTicket(
            TicketTypeEntity ticketTypeEntity,
            HolderEntity holderEntity,
            OrderEntity orderEntity,
            PaymentStatusEntity paymentStatusEntity
    ){
        int ageRestriction = ticketTypeEntity.getEventEntity().getAgeRestriction();
        if(ageRestriction > 0){
            long ageHolder = ChronoUnit.YEARS.between(LocalDate.now(), holderEntity.getBirthDate());

            if(ageHolder > ageRestriction){
                throw new InvalidAgeForEventException(holderEntity.getName());
            }
        }

        if(holderEntity.getTicketEntities() != null && !holderEntity.getTicketEntities().isEmpty()){
            List<TicketEntity> holderHasTypeTicket = holderEntity.getTicketEntities().stream().filter(
                    (ticket) -> ticket.getPaymentStatusEntity().getDescription().equals(PaymentStatusEnum.APPROVED.getName())
                    && ticket.getTicketTypeEntity().getEventEntity().equals(ticketTypeEntity.getEventEntity())
            ).toList();

            if(!holderHasTypeTicket.isEmpty()){
                throw new HolderAlreadyHasTicketException(holderEntity.getName(), ticketTypeEntity.getId().toString());
            }
        }

        return new TicketEntity(
                ticketTypeEntity,
                holderEntity,
                orderEntity,
                paymentStatusEntity
        );
    }
}
