package com.tcc.api_ticket_sales.factory;

import com.tcc.api_ticket_sales.domain.entity.HolderEntity;

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
}
