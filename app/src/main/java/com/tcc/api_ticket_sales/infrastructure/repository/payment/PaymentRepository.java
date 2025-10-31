package com.tcc.api_ticket_sales.infrastructure.repository.payment;

import com.tcc.api_ticket_sales.domain.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.rmi.server.UID;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UID> {
}
