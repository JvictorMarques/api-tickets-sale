package com.tcc.api_ticket_sales.domain.service;

import com.tcc.api_ticket_sales.domain.entity.HolderEntity;
import com.tcc.api_ticket_sales.domain.exception.BusinessException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.tcc.api_ticket_sales.factory.HolderFactory.createHolderEntityWithId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class HolderDomainServiceTest {

    @Test
    @Tag("unit")
    void creatHolderEntity_shouldReturnHolderEntity_whenHolderEntityOK() {
        HolderDomainService holderDomainService = new HolderDomainService();

        HolderEntity holderEntity = createHolderEntityWithId();
        HolderEntity savedHolder = holderDomainService.creatHolderEntity(holderEntity);

        assertNotNull(savedHolder);
        assertEquals(holderEntity.getName(), savedHolder.getName());
    }

    @Test
    @Tag("unit")
    void creatHolderEntity_shouldThrowBusinessException_whenBirthDateIsAfterNow() {
        HolderDomainService holderDomainService = new HolderDomainService();

        HolderEntity holderEntity = createHolderEntityWithId();
        holderEntity.setBirthDate(LocalDate.now().plusDays(2));

        assertThrows(
                BusinessException.class,
                () -> holderDomainService.creatHolderEntity(holderEntity)
        );
    }

    @Test
    @Tag("unit")
    void creatOrReturnExistsHolderEntity_shouldReturnHolder_whenHolderEntityExists() {
        HolderDomainService holderDomainService = new HolderDomainService();

        HolderEntity holderEntity1 = createHolderEntityWithId();
        HolderEntity holderEntity2 = createHolderEntityWithId();

        HolderEntity holderEntity = holderDomainService.creatOrReturnExistsHolderEntity(holderEntity2, List.of(holderEntity1));

        assertEquals(holderEntity, holderEntity1);
    }

    @Test
    @Tag("unit")
    void creatOrReturnExistsHolderEntity_shouldReturnHolder_whenHolderEntityExistsWithDeletedAt() {
        HolderDomainService holderDomainService = spy(new HolderDomainService());
        HolderEntity holderEntity1 = createHolderEntityWithId();
        holderEntity1.setDeletedAt(LocalDateTime.now());
        HolderEntity holderEntity2 = createHolderEntityWithId();


        HolderEntity holderEntity = holderDomainService.creatOrReturnExistsHolderEntity(holderEntity2, List.of(holderEntity1));

        assertEquals(holderEntity, holderEntity2);

        verify(holderDomainService).creatHolderEntity(holderEntity2);
    }

    @Test
    @Tag("unit")
    void creatOrReturnExistsHolderEntity_shouldReturnHolder_whenHolderEntityExistsIsEmpty() {
        HolderDomainService holderDomainService = spy(new HolderDomainService());
        HolderEntity holderEntity2 = createHolderEntityWithId();

        HolderEntity holderEntity = holderDomainService.creatOrReturnExistsHolderEntity(holderEntity2, List.of());

        assertEquals(holderEntity, holderEntity2);

        verify(holderDomainService).creatHolderEntity(holderEntity2);
    }
}