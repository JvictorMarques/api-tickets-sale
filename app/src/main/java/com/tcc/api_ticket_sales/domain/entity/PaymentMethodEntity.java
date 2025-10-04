package com.tcc.api_ticket_sales.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment_methods")
public class PaymentMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;

    @OneToMany(mappedBy = "paymentMethodEntity")
    private List<PaymentEntity> paymentEntities;

    public PaymentMethodEntity(String description) {
        this.description = description;
    }
}
