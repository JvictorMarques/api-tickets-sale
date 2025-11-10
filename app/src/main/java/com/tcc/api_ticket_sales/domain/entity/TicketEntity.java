package com.tcc.api_ticket_sales.domain.entity;

import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name="tickets")
public class TicketEntity extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_type_id")
    private TicketTypeEntity ticketTypeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holder_id")
    private HolderEntity holderEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_status_id")
    private PaymentStatusEntity paymentStatusEntity;

    public TicketEntity(
            TicketTypeEntity ticketTypeEntity,
            HolderEntity holderEntity,
            OrderEntity orderEntity,
            PaymentStatusEntity paymentStatusEntity
    ){
        if(ticketTypeEntity == null){
            throw new BusinessException("O tipo do ingresso não pode ser nulo");
        }

        if(holderEntity == null){
            throw new BusinessException("O titular do ingresso não pode ser nulo");
        }

        if(orderEntity == null){
            throw new BusinessException("A ordem do ingresso não pode ser nula");
        }

        if(paymentStatusEntity == null){
            throw new BusinessException("O status de pagamento do ingresso não pode ser nulo");
        }

        this.ticketTypeEntity = ticketTypeEntity;
        this.holderEntity = holderEntity;
        this.orderEntity = orderEntity;
        this.paymentStatusEntity = paymentStatusEntity;
    }
}
