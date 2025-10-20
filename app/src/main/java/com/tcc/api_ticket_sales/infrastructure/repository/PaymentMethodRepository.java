package com.tcc.api_ticket_sales.infrastructure.repository;

import com.tcc.api_ticket_sales.domain.entity.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.rmi.server.UID;
import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, UID> {
    Optional<PaymentMethodEntity> findByDescription(String description);
}
