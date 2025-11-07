package com.tcc.api_ticket_sales.domain.entity;

import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class OrderEntity extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "orderEntity")
    private List<PaymentEntity> paymentEntities;

    @OneToMany(mappedBy = "orderEntity")
    private List<TicketEntity>  ticketEntities;

    private OrderEntity (
            BigDecimal totalPrice
    ){
        if(totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) <= 0){
            throw new BusinessException("O valor total do pedido inválido.");
        }

        this.totalPrice = totalPrice;
    }


    public static OrderEntity of(
            BigDecimal totalPrice
    ){
        return new OrderEntity(totalPrice);
    }
}
