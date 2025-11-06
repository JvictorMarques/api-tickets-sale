package com.tcc.api_ticket_sales.domain.service;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.enums.PaymentStatusEnum;
import com.tcc.api_ticket_sales.domain.exception.EventAgeRestrictionIncreaseNotAllowedException;
import com.tcc.api_ticket_sales.domain.exception.EventCapacityReductionNotAllowedException;
import org.springframework.stereotype.Component;

@Component
public class EventDomainService {

    public void updateEvent(EventEntity eventEntityOld, EventEntity eventEntity){
        if(eventEntityOld.getTicketTypeEntities() == null || eventEntityOld.getTicketTypeEntities().isEmpty()) return;

        int sumCapacityTicketsTypes = sumCapacityTicketsTypes(eventEntityOld);
        if(sumCapacityTicketsTypes > eventEntity.getCapacity()){
            throw new EventCapacityReductionNotAllowedException(sumCapacityTicketsTypes);
        }

        if(eventEntityOld.getAgeRestriction() < eventEntity.getAgeRestriction() && countTicketsPurchased(eventEntityOld) > 0){
            throw new EventAgeRestrictionIncreaseNotAllowedException();
        }


        eventEntityOld.getTicketTypeEntities().forEach(ticketTypeEntity -> {
            if(ticketTypeEntity.getDateFinal().isAfter(eventEntity.getDateFinal())) ticketTypeEntity.setDateFinal(eventEntity.getDateFinal());
        });
    }

    private long countTicketsPurchased(EventEntity eventEntity){
        return eventEntity.getTicketTypeEntities().stream().mapToLong(ticketTypeEntity ->
            ticketTypeEntity.getTicketEntities().stream().filter(ticket ->
                    ticket.getPaymentStatusEntity().getDescription().equals(PaymentStatusEnum.APPROVED.getName())
                            && ticket.getDeletedAt() == null
            ).count()
        ).sum();
    }

    private int sumCapacityTicketsTypes(EventEntity eventEntity){
        return eventEntity.getTicketTypeEntities().stream().mapToInt(TicketTypeEntity::getCapacity).sum();
    }
}
