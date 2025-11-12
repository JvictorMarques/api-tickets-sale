package com.tcc.api_ticket_sales.infrastructure.repository.event;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface EventRepository extends JpaRepository<EventEntity, UUID>, JpaSpecificationExecutor<EventEntity> {
}
