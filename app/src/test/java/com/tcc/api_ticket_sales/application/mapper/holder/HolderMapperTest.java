package com.tcc.api_ticket_sales.application.mapper.holder;

import com.tcc.api_ticket_sales.application.dto.holder.HolderCreateRequestDTO;
import com.tcc.api_ticket_sales.domain.entity.HolderEntity;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.tcc.api_ticket_sales.factory.HolderFactory.createHolderCreateRequestDTO;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HolderMapperTest {

    @InjectMocks
    private HolderMapper mapper;

    @Tag("unit")
    @Test
    void shouldMapHolderHolderCreateRequestDTOToHolderEntity() {
        HolderCreateRequestDTO holderCreateRequestDTO = createHolderCreateRequestDTO();

        HolderEntity holderEntity = mapper.fromHolderCreateRequestDTOToHolderEntity(holderCreateRequestDTO);
        assertEquals(holderEntity.getName(), holderCreateRequestDTO.getName());
        assertEquals(holderEntity.getEmail(), holderCreateRequestDTO.getEmail());
        assertEquals(holderEntity.getBirthDate(), holderCreateRequestDTO.getBirthDate());
    }
}