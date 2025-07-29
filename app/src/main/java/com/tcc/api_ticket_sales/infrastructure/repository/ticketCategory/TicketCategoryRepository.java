package com.tcc.api_ticket_sales.infrastructure.repository.ticketCategory;

import com.tcc.api_ticket_sales.entity.EventCategoryEntity;
import com.tcc.api_ticket_sales.entity.TicketCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TicketCategoryRepository extends JpaRepository<TicketCategoryEntity, UUID> {
    Optional<TicketCategoryEntity> findByName(String name);
}
