package com.tcc.api_ticket_sales.domain.service;

import com.tcc.api_ticket_sales.domain.enums.PaymentStatusEnum;
import com.tcc.api_ticket_sales.domain.exception.EventClosedException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeCapacityReductionNotAllowedException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeCapacityExceedException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeCapacityExceedsEventLimitException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeClosedException;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeDatesExceedsEventDateException;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.exception.TicketTypeDeletionNotAllowedException;
import com.tcc.api_ticket_sales.domain.model.TicketBuyModel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

        int sumCapacity = this.sumCapacityTicketTypeInEvent(ticketTypesExists);
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

    public TicketTypeEntity updateTicketType(
            TicketTypeEntity ticketType
    ){
        if(ticketType.getDeletedAt() != null){
            throw new TicketTypeClosedException();
        }

        EventEntity eventEntity = ticketType.getEventEntity();
        if(eventEntity.isClosed()){
            throw new EventClosedException();
        }

        long ticketsPurchased = this.countTicketsPurchased(ticketType);
        if(ticketType.getCapacity() < ticketsPurchased){
            throw new TicketTypeCapacityReductionNotAllowedException(ticketsPurchased);
        }

        int sumCapacity = sumCapacityTicketTypeInEvent(ticketType.getEventEntity().getTicketTypeEntities());
        if((sumCapacity + ticketType.getCapacity()) > eventEntity.getCapacity()){
            int availableCapacity = eventEntity.getCapacity() - sumCapacity;
            throw new TicketTypeCapacityExceedsEventLimitException(availableCapacity);
        }

        if(ticketType.getDateInitial().isAfter(eventEntity.getDateFinal()) ||
                (ticketType.getDateFinal() != null && ticketType.getDateFinal().isAfter(eventEntity.getDateFinal())
                )){
            throw new TicketTypeDatesExceedsEventDateException();
        }

        return ticketType;
    }

    public TicketTypeEntity deleteTicketType(
            TicketTypeEntity ticketType
    ){
        if(this.countTicketsPurchased(ticketType) > 0){
            throw new TicketTypeDeletionNotAllowedException();
        }

        ticketType.setDeletedAt(LocalDateTime.now());
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
        long countTicketsBuy = this.countTicketsPurchased(ticketTypeEntity);

        if ((countTicketsBuy + requestedQuantity) > ticketTypeEntity.getCapacity()) {
            throw new TicketTypeCapacityExceedException();
        }
    }

    public BigDecimal calculateTotalPrice(List<TicketBuyModel> tickets){
        return tickets.stream().map(
                ticketBuyModel -> ticketBuyModel.getTicketTypeEntity().getPrice()
                        .multiply(BigDecimal.valueOf(ticketBuyModel.getQuantity()))
        ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private long countTicketsPurchased(TicketTypeEntity ticketTypeEntity){
        return ticketTypeEntity.getTicketEntities().stream().filter(ticket ->
                ticket.getPaymentStatusEntity().getDescription().equals(PaymentStatusEnum.APPROVED.getName())
                && ticket.getDeletedAt() == null
        ).count();
    }

    private int sumCapacityTicketTypeInEvent(List<TicketTypeEntity> ticketTypes){
        return ticketTypes.stream()
                .filter(ticketTypeEntity -> ticketTypeEntity.getDeletedAt() == null)
                .mapToInt(TicketTypeEntity::getCapacity).sum();
    }
}
