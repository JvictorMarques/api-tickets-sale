package com.tcc.api_ticket_sales.infrastructure.repository.holder;

import com.tcc.api_ticket_sales.entity.HolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HolderRepository extends JpaRepository<HolderEntity, UUID> {
}
