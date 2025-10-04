package com.tcc.api_ticket_sales.infrastructure.repository;

import com.tcc.api_ticket_sales.domain.entity.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.rmi.server.UID;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, UID> {
}
