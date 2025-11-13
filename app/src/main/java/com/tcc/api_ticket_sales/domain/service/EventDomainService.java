package com.tcc.api_ticket_sales.domain.service;

import com.tcc.api_ticket_sales.application.exception.EventNotFoundException;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.enums.PaymentStatusEnum;
import com.tcc.api_ticket_sales.domain.exception.EventAgeRestrictionIncreaseNotAllowedException;
import com.tcc.api_ticket_sales.domain.exception.EventCapacityReductionNotAllowedException;
import com.tcc.api_ticket_sales.domain.exception.EventClosedException;
import com.tcc.api_ticket_sales.domain.exception.EventDeletionNotAllowedException;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.tcc.api_ticket_sales.domain.utils.CheckDate.checkDateInitialGreaterThanDateFinal;

@Component
public class EventDomainService {

    public void updateEvent(EventEntity eventEntityOld, EventEntity eventEntity){
        checkDateInitialGreaterThanDateFinal(eventEntity.getDateInitial(), eventEntity.getDateFinal());

        if(eventEntityOld.isClosed()) throw new EventClosedException();

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

    public void deletedEvent(EventEntity eventEntity){
        checkIsDeleted(eventEntity);

        if(eventEntity.getTicketTypeEntities() == null || eventEntity.getTicketTypeEntities().isEmpty()){
            eventEntity.setDeletedAt(LocalDateTime.now());
            return;
        }

        if(countTicketsPurchased(eventEntity) > 0){
            throw new EventDeletionNotAllowedException();
        }

        eventEntity.setDeletedAt(LocalDateTime.now());
        eventEntity.getTicketTypeEntities().forEach(ticketTypeEntity -> {
            ticketTypeEntity.setDeletedAt(LocalDateTime.now());
        });
    }

    private long countTicketsPurchased(EventEntity eventEntity){
        return eventEntity.getTicketTypeEntities().stream().mapToLong(ticketTypeEntity -> {
            if(ticketTypeEntity.getTicketEntities() == null || ticketTypeEntity.getTicketEntities().isEmpty()) return 0;

            return ticketTypeEntity.getTicketEntities().stream().filter(ticket ->
                    ticket.getPaymentStatusEntity().getDescription().equals(PaymentStatusEnum.APPROVED.getName())
                            && ticket.getDeletedAt() == null
            ).count();
        }).sum();
    }

    private int sumCapacityTicketsTypes(EventEntity eventEntity){
        return eventEntity.getTicketTypeEntities().stream().mapToInt(TicketTypeEntity::getCapacity).sum();
    }

    public void checkIsDeleted(EventEntity event) {
        if (event.getDeletedAt() != null) {
            throw new EventNotFoundException(event.getId().toString());
        }
    }
}
