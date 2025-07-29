package com.tcc.api_ticket_sales.infrastructure.repository.eventLocation;

import com.tcc.api_ticket_sales.entity.EventLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventLocationRepository extends JpaRepository<EventLocationEntity, UUID> {
    Optional<EventLocationEntity> findByName(String name);
}
