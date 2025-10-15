package com.tcc.api_ticket_sales.infrastructure.repository.ticket;

import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {

    List<TicketEntity> findByEventEntityIdAndNameIgnoreCase(UUID eventId, String name);
    List<TicketEntity> findByEventEntityId(UUID eventId);
}
