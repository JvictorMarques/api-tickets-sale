package com.tcc.api_ticket_sales.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment_statuses")
public class PaymentStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;

    @OneToMany(mappedBy = "paymentStatusEntity")
    private List<PaymentEntity> paymentEntities;

    public PaymentStatusEntity(String description) {
        this.description = description;
    }
}
