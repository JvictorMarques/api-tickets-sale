package com.tcc.api_ticket_sales.infrastructure.repository.eventCategory;

import com.tcc.api_ticket_sales.entity.EventCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventCategoryRepository extends JpaRepository<EventCategoryEntity, UUID> {
    Optional<EventCategoryEntity> findByName(String name);
}
