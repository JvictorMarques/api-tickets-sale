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
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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

    @Column(nullable = false)
    private String preferenceId;

    @OneToMany(mappedBy = "orderEntity")
    private List<PaymentEntity> paymentEntities;

    @OneToMany(mappedBy = "orderEntity")
    private List<TicketEntity>  ticketEntities;

    private OrderEntity (
            String preferenceId,
            BigDecimal totalPrice
    ){
        if(preferenceId.isBlank()){
            throw new BusinessException("Preferência do pedido inválida.");
        }
        if(totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) <= 0){
            throw new BusinessException("O valor total do pedido inválido.");
        }

        this.preferenceId = preferenceId;
        this.totalPrice = totalPrice;
    }


    public static OrderEntity of(
            String preferenceId,
            BigDecimal totalPrice
    ){
        return new OrderEntity(preferenceId, totalPrice);
    }
}
