package com.tcc.api_ticket_sales.domain.entity;

import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEntity extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethodEntity paymentMethodEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_status_id")
    private PaymentStatusEntity paymentStatusEntity;

    private PaymentEntity(
        BigDecimal amount,
        OrderEntity orderEntity,
        PaymentMethodEntity paymentMethodEntity,
        PaymentStatusEntity paymentStatusEntity
    ){
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new BusinessException("O valor do pagamento inválido");
        }

        if(orderEntity == null){
            throw new BusinessException("Ordem inválida");
        }

        if(paymentMethodEntity == null){
            throw new BusinessException("Método de pagamento inválido");
        }

        if(paymentStatusEntity == null){
            throw new BusinessException("Status de pagamento inválido");
        }

        this.amount = amount;
        this.orderEntity = orderEntity;
        this.paymentMethodEntity = paymentMethodEntity;
        this.paymentStatusEntity = paymentStatusEntity;
    }

    public static PaymentEntity of(
            BigDecimal amount,
            OrderEntity orderEntity,
            PaymentMethodEntity paymentMethodEntity,
            PaymentStatusEntity paymentStatusEntity
    ){
        return  new PaymentEntity(amount, orderEntity, paymentMethodEntity, paymentStatusEntity);
    }
}
