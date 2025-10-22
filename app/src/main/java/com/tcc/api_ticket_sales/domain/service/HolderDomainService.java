package com.tcc.api_ticket_sales.domain.service;

import com.tcc.api_ticket_sales.domain.entity.HolderEntity;
import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class HolderDomainService {

    public HolderEntity creatOrReturnExistsHolderEntity(HolderEntity holderEntity, List<HolderEntity> holderEntityFindList) {
        Optional<HolderEntity> first = holderEntityFindList.stream().filter(holder -> holder.getDeletedAt() != null).findFirst();

        return first.orElseGet(() -> creatHolderEntity(holderEntity));
    }

    public HolderEntity creatHolderEntity(HolderEntity holderEntity){
        if(holderEntity.getBirthDate() == null || holderEntity.getBirthDate().isAfter(LocalDate.now())){
            throw new BusinessException("Data de nascimento do titular inválida");
        }

        return holderEntity;
    }
}
