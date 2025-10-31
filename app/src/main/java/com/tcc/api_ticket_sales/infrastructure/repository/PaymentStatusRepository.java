package com.tcc.api_ticket_sales.infrastructure.repository;

import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatusEntity, UUID> {
    Optional<PaymentStatusEntity> findByDescription(String description);
}
