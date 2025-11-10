package com.tcc.api_ticket_sales.infrastructure.repository.holder;

import com.tcc.api_ticket_sales.domain.entity.HolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface HolderRepository extends JpaRepository<HolderEntity, UUID>, JpaSpecificationExecutor<HolderEntity> {

    List<HolderEntity> findByNameAndEmail(String name, String email);
}
