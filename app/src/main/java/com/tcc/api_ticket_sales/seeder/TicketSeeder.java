package com.tcc.api_ticket_sales.seeder;

import com.tcc.api_ticket_sales.entity.TicketEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@RequiredArgsConstructor
@Component
public class TicketSeeder {

    private final TicketRepository ticketRepository;
    private final TicketCategorySeeder ticketCategorySeeder;
    private final EventSeeder eventSeeder;

    public void seed(){
        if(ticketRepository.count() > 0) return;

        ticketRepository.save(TicketEntity.builder()
                .price(new BigDecimal(100))
                .ticketCategoryEntity(ticketCategorySeeder.seedExists("Inteira"))
                .eventEntity(eventSeeder.seedExists("Arquitetura limpa e seus benefícios"))
                .build()
        );

        ticketRepository.save(TicketEntity.builder()
                .price(new BigDecimal(50))
                .ticketCategoryEntity(ticketCategorySeeder.seedExists("Meia-Entrada"))
                .eventEntity(eventSeeder.seedExists("Arquitetura limpa e seus benefícios"))
                .build()
        );
    }
}
