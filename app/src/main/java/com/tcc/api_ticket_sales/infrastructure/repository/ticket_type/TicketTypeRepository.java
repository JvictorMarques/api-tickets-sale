package com.tcc.api_ticket_sales.infrastructure.repository.ticket_type;

import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketTypeRepository extends JpaRepository<TicketTypeEntity, UUID> {

    List<TicketTypeEntity> findByEventEntityIdAndNameIgnoreCase(UUID eventId, String name);
    List<TicketTypeEntity> findByEventEntityId(UUID eventId);
}
