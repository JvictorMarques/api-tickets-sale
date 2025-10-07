package com.tcc.api_ticket_sales.factory;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.interfaces.dto.event.EventResponseDTO;
import org.instancio.Instancio;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import static org.instancio.Select.field;

public class EventFactory {

    public static EventEntity createEventEntityWithoutId(){
        String randomName = "Evento " + ThreadLocalRandom.current().nextInt(100, 999);
        int randomCapacity = ThreadLocalRandom.current().nextInt(50, 201);

        return EventEntity.of(
                randomName,
                "Descricao Teste",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                randomCapacity, // capacidade fixa
                18,  // idade mínima fixa
                "São Paulo" // local fixo
        );
    }

    public static EventResponseDTO createEventResponseDTO(){
        return Instancio.create(EventResponseDTO.class);
    }
}
