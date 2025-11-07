package com.tcc.api_ticket_sales.factory;

import com.tcc.api_ticket_sales.application.dto.event.EventUpdateRequestDTO;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.application.dto.event.EventCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventResponseDTO;
import org.instancio.Instancio;

import java.time.LocalDateTime;
import java.util.UUID;
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

    public static EventEntity createEventEntityWithId(){
        String randomName = "Evento " + ThreadLocalRandom.current().nextInt(100, 999);
        int randomCapacity = ThreadLocalRandom.current().nextInt(50, 201);

        EventEntity entity = EventEntity.of(
                randomName,
                "Descricao Teste",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                randomCapacity, // capacidade fixa
                18,  // idade mínima fixa
                "São Paulo" // local fixo
        );

        entity.setId(UUID.randomUUID());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        return entity;
    }

    public static EventResponseDTO createEventResponseDTO(){
        return Instancio.create(EventResponseDTO.class);
    }

    public static EventCreateRequestDTO createEventCreateDTODefault(){
        return Instancio.create(EventCreateRequestDTO.class);
    }

    public static EventCreateRequestDTO createEventCreateDTOValid(){
        return Instancio.of(EventCreateRequestDTO.class)
                .set(field(EventCreateRequestDTO::getCapacity), 300)
                .set(field(EventCreateRequestDTO::getDateInitial), LocalDateTime.now().plusDays(1))
                .set(field(EventCreateRequestDTO::getDateFinal), LocalDateTime.now().plusDays(1).plusHours(3))
                .create();
    }

    public static EventCreateRequestDTO createEventCreateDTOInvalid(){
        return Instancio.of(EventCreateRequestDTO.class)
                .set(field(EventCreateRequestDTO::getName), "")
                .set(field(EventCreateRequestDTO::getLocation), "")
                .set(field(EventCreateRequestDTO::getCapacity), 0)
                .set(field(EventCreateRequestDTO::getDateInitial), LocalDateTime.now().minusDays(1))
                .set(field(EventCreateRequestDTO::getDateFinal), LocalDateTime.now().minusDays(1))
                .create();
    }

    public static EventUpdateRequestDTO createEventUpdateRequestDTO(){
        return Instancio.create(EventUpdateRequestDTO.class);
    }

    public static EventUpdateRequestDTO createEventUpdateRequestDTOInvalid(){
        return Instancio.of(EventUpdateRequestDTO.class)
                .set(field(EventUpdateRequestDTO::getName), "")
                .set(field(EventUpdateRequestDTO::getLocation), "")
                .set(field(EventUpdateRequestDTO::getCapacity), 0)
                .set(field(EventUpdateRequestDTO::getDateInitial), LocalDateTime.now().minusDays(1))
                .set(field(EventUpdateRequestDTO::getDateFinal), LocalDateTime.now().minusDays(1))
                .create();
    }
}
