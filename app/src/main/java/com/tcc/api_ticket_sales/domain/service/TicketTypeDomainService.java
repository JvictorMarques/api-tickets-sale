package com.tcc.api_ticket_sales.domain.service;

import com.tcc.api_ticket_sales.domain.enums.PaymentStatusEnum;
import com.tcc.api_ticket_sales.domain.exception.EventClosedException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeCapacityExceedException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeCapacityExceedsEventLimitException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeClosedException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeDatesExceedsEventDateException;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TicketTypeDomainService {

    public TicketTypeEntity createTicketType(
            EventEntity eventEntity,
            List<TicketTypeEntity> ticketTypesExists,
            TicketTypeEntity ticketType)
    {
        if(eventEntity.isClosed()){
            throw new EventClosedException();
        }

        int sumCapacity = ticketTypesExists.stream().mapToInt(TicketTypeEntity::getCapacity).sum();
        if((sumCapacity + ticketType.getCapacity()) > eventEntity.getCapacity()){
            int availableCapacity = eventEntity.getCapacity() - sumCapacity;
            throw new TicketTypeCapacityExceedsEventLimitException(availableCapacity);
        }

        if(ticketType.getDateInitial().isAfter(eventEntity.getDateFinal()) ||
                (ticketType.getDateFinal() != null && ticketType.getDateFinal().isAfter(eventEntity.getDateFinal())
                )){
            throw new TicketTypeDatesExceedsEventDateException();
        }


        if(ticketType.getDateFinal() == null){
            ticketType.setDateFinal(eventEntity.getDateFinal());
        }

        return ticketType;
    }

    public void validateTicketTypeSale(TicketTypeEntity ticketTypeEntity){
        if(ticketTypeEntity.getDeletedAt() != null){
            throw new TicketTypeClosedException();
        }

        if(ticketTypeEntity.getDateFinal().isBefore(LocalDateTime.now())){
            throw new TicketTypeClosedException();
        }

        if(ticketTypeEntity.getEventEntity().isClosed()){
            throw new EventClosedException();
        }
    }

    public void validateCapacity (TicketTypeEntity ticketTypeEntity, int requestedQuantity){
        long countTicketsBuy = ticketTypeEntity.getTicketEntities().stream().filter(
                ticket -> ticket.getPaymentStatusEntity().getDescription().equals(PaymentStatusEnum.APPROVED.getName())
        ).count();

        if ((countTicketsBuy + requestedQuantity) > ticketTypeEntity.getCapacity()) {
            throw new TicketTypeCapacityExceedException();
        }
    }

}
