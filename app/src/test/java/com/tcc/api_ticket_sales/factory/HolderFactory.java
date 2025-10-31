package com.tcc.api_ticket_sales.factory;

import com.tcc.api_ticket_sales.application.dto.holder.HolderCreateRequestDTO;
import com.tcc.api_ticket_sales.domain.entity.HolderEntity;
import org.instancio.Instancio;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class HolderFactory {
    public static HolderEntity createHolderEntityWithId(){
        String randomName = "Holder " + ThreadLocalRandom.current().nextInt(100, 999);
        HolderEntity holder =  HolderEntity.of(
                randomName,
                "Descrição Teste",
                LocalDate.now()
        );

        holder.setId(UUID.randomUUID());

        return holder;
    }

    public static HolderEntity createHolderEntityWithoutId(){
        String randomName = "Holder " + ThreadLocalRandom.current().nextInt(100, 999);

        return HolderEntity.of(
                randomName,
                "Descrição Teste",
                LocalDate.now()
        );
    }

    public static HolderCreateRequestDTO createHolderCreateRequestDTO(){
        return Instancio.create(HolderCreateRequestDTO.class);
    }
}
