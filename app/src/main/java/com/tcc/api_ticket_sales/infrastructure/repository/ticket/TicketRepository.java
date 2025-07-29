package com.tcc.api_ticket_sales.infrastructure.repository.ticket;

import com.tcc.api_ticket_sales.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {
}
