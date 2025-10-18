package com.tcc.api_ticket_sales.interfaces.mapper.holder;

import com.tcc.api_ticket_sales.domain.entity.HolderEntity;
import com.tcc.api_ticket_sales.interfaces.dto.holder.HolderCreateRequestDTO;
import org.springframework.stereotype.Component;


@Component
public class HolderMapper {

    public HolderEntity fromHolderCreateRequestDTOToHolderEntity (HolderCreateRequestDTO dto){
        return HolderEntity.of(
                dto.getName(),
                dto.getEmail(),
                dto.getBirthDate()
        );
    }
}
