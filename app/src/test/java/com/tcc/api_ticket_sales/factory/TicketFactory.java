package com.tcc.api_ticket_sales.factory;

import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.TicketCreateRequestDTO;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.TicketCreateResponseDTO;
import org.instancio.Instancio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.instancio.Select.field;

public class TicketFactory {

    public static TicketEntity createTicketEntityWithoutId(){
        String randomName = "Ticket " + ThreadLocalRandom.current().nextInt(100, 999);
        int randomCapacity = ThreadLocalRandom.current().nextInt(50, 201);
        EventEntity eventEntity = EventFactory.createEventEntityWithoutId();

        return TicketEntity.of(
                randomName,
                "Descricao Teste",
                BigDecimal.valueOf(30),
                eventEntity,
                randomCapacity,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
    }

    public static TicketEntity createTicketEntityWithId(){
        String randomName = "Ticket " + ThreadLocalRandom.current().nextInt(100, 999);
        int randomCapacity = ThreadLocalRandom.current().nextInt(50, 201);
        EventEntity eventEntity = EventFactory.createEventEntityWithoutId();

        TicketEntity ticket = TicketEntity.of(
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

    public static TicketCreateResponseDTO createTicketCreateResponseDTODefault(){
        return Instancio.create(TicketCreateResponseDTO.class);
    }

    public static TicketCreateRequestDTO createTicketCreateRequestDTOValid(){
        return Instancio.of(TicketCreateRequestDTO.class)
                .set(field(TicketCreateRequestDTO::getPrice), BigDecimal.valueOf(90))
                .set(field(TicketCreateRequestDTO::getCapacity), 100)
                .set(field(TicketCreateRequestDTO::getDateInitial), LocalDateTime.now().plusDays(1))
                .set(field(TicketCreateRequestDTO::getDateFinal), LocalDateTime.now().plusDays(1).plusHours(3))
                .create();
    }

    public static TicketCreateRequestDTO createTicketCreateRequestDTOInvalid(){
        return Instancio.of(TicketCreateRequestDTO.class)
                .set(field(TicketCreateRequestDTO::getName), "")
                .set(field(TicketCreateRequestDTO::getPrice), BigDecimal.valueOf(9))
                .set(field(TicketCreateRequestDTO::getCapacity), 0)
                .set(field(TicketCreateRequestDTO::getDateInitial), LocalDateTime.now().minusDays(1))
                .set(field(TicketCreateRequestDTO::getDateFinal), LocalDateTime.now().minusDays(1))
                .create();
    }
}
