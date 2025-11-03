package com.tcc.api_ticket_sales.infrastructure.repository.ticket_type;

import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TicketTypeRepository extends JpaRepository<TicketTypeEntity, UUID> {

    @Query("""
        SELECT t
        FROM TicketTypeEntity t
        WHERE t.eventEntity.id = :eventId
          AND LOWER(t.name) = LOWER(:name)
          AND t.deletedAt IS NULL
    """)
    List<TicketTypeEntity> findByEventEntityIdAndNameIgnoreCase(
            @Param("eventId") UUID eventId,
            @Param("name") String name
    );
    List<TicketTypeEntity> findByEventEntityId(UUID eventId);
}
