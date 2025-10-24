package com.tcc.api_ticket_sales.infrastructure.repository.order;

import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
