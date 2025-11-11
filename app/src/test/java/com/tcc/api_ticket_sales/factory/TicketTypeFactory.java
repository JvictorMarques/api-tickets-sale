package com.tcc.api_ticket_sales.factory;

import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeUpdateRequestDTO;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeResponseDTO;
import org.instancio.Instancio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.instancio.Select.field;

public class TicketTypeFactory {

    public static TicketTypeEntity createTicketTypeEntityWithoutId(){
        String randomName = "Ticket " + ThreadLocalRandom.current().nextInt(100, 999);
        int randomCapacity = ThreadLocalRandom.current().nextInt(50, 201);
        EventEntity eventEntity = EventFactory.createEventEntityWithoutId();

        return TicketTypeEntity.of(
                randomName,
                "Descricao Teste",
                BigDecimal.valueOf(30),
                eventEntity,
                randomCapacity,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
    }

    public static TicketTypeEntity createTicketTypeEntityWithId(){
        String randomName = "Ticket " + ThreadLocalRandom.current().nextInt(100, 999);
        int randomCapacity = ThreadLocalRandom.current().nextInt(50, 201);
        EventEntity eventEntity = EventFactory.createEventEntityWithoutId();

        TicketTypeEntity ticket = TicketTypeEntity.of(
                randomName,
                "Descricao Teste",
                BigDecimal.valueOf(30),
                eventEntity,
                randomCapacity,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        ticket.setId(UUID.randomUUID());

        return ticket;
    }

    public static TicketTypeEntity createTicketTypeEntityWithoutId(EventEntity eventEntity){
        String randomName = "Ticket " + ThreadLocalRandom.current().nextInt(100, 999);
        int randomCapacity = ThreadLocalRandom.current().nextInt(50, 201);

        return TicketTypeEntity.of(
                randomName,
                "Descricao Teste",
                BigDecimal.valueOf(30),
                eventEntity,
                randomCapacity,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
    }

    public static TicketTypeResponseDTO createTicketTypeCreateResponseDTODefault(){
        return Instancio.create(TicketTypeResponseDTO.class);
    }

    public static TicketTypeCreateRequestDTO createTicketTypeCreateRequestDTOValid(){
        return Instancio.of(TicketTypeCreateRequestDTO.class)
                .set(field(TicketTypeCreateRequestDTO::getPrice), BigDecimal.valueOf(90))
                .set(field(TicketTypeCreateRequestDTO::getCapacity), 100)
                .set(field(TicketTypeCreateRequestDTO::getDateInitial), LocalDateTime.now().plusDays(1))
                .set(field(TicketTypeCreateRequestDTO::getDateFinal), LocalDateTime.now().plusDays(1).plusHours(3))
                .create();
    }

    public static TicketTypeCreateRequestDTO createTicketTypeCreateRequestDTOInvalid(){
        return Instancio.of(TicketTypeCreateRequestDTO.class)
                .set(field(TicketTypeCreateRequestDTO::getName), "")
                .set(field(TicketTypeCreateRequestDTO::getPrice), BigDecimal.valueOf(9))
                .set(field(TicketTypeCreateRequestDTO::getCapacity), 0)
                .set(field(TicketTypeCreateRequestDTO::getDateInitial), LocalDateTime.now().minusDays(1))
                .set(field(TicketTypeCreateRequestDTO::getDateFinal), LocalDateTime.now().minusDays(1))
                .create();
    }

    public static TicketTypeUpdateRequestDTO createTicketTypeUpdateRequestDTODefault(){
        return Instancio.create(TicketTypeUpdateRequestDTO.class);
    }

    public static TicketTypeUpdateRequestDTO createTicketTypeUpdateRequestDTOInvalid(){
        return Instancio.of(TicketTypeUpdateRequestDTO.class)
                .set(field(TicketTypeUpdateRequestDTO::getName), "")
                .set(field(TicketTypeUpdateRequestDTO::getPrice), BigDecimal.valueOf(9))
                .set(field(TicketTypeUpdateRequestDTO::getCapacity), 0)
                .set(field(TicketTypeUpdateRequestDTO::getDateInitial), LocalDateTime.now().minusDays(1))
                .set(field(TicketTypeUpdateRequestDTO::getDateFinal), LocalDateTime.now().minusDays(1))
                .create();
    }
}
