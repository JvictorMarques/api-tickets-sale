package com.tcc.api_ticket_sales.seeder;

import com.tcc.api_ticket_sales.entity.EventEntity;
import com.tcc.api_ticket_sales.entity.TicketCategoryEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class EventSeeder {
    private final EventRepository eventRepository;
    private final EventLocationSeeder eventLocationSeeder;
    private final EventCategorySeeder eventCategorySeeder;

    public void seed(){
        if(eventRepository.count() > 0) return;

        eventRepository.save(EventEntity.builder()
                .name("Arquitetura limpa e seus benefícios")
                .dateFinal(LocalDateTime.now().plusHours(2))
                .eventCategoryEntity(eventCategorySeeder.seedExists("Palestra"))
                .eventLocationEntity(eventLocationSeeder.seedExists("Espaço Cultural"))
                .isAdultOnly(true)
                .maxPeople(2)
                .build()
        );

        eventRepository.save(EventEntity.builder()
                .name("Show dos Beatles")
                .dateFinal(LocalDateTime.now().plusHours(3))
                .eventCategoryEntity(eventCategorySeeder.seedExists("Show"))
                .eventLocationEntity(eventLocationSeeder.seedExists("Domus Hall"))
                .isAdultOnly(true)
                .maxPeople(300)
                .build()
        );

        eventRepository.save(EventEntity.builder()
                .name("Testes de software e seus desafios")
                .dateFinal(LocalDateTime.now().plusHours(3))
                .eventCategoryEntity(eventCategorySeeder.seedExists("Palestra"))
                .eventLocationEntity(eventLocationSeeder.seedExists("Domus Hall"))
                .isAdultOnly(true)
                .maxPeople(300)
                .build()
        );
    }

    public EventEntity seedExists(String eventName){
        return eventRepository.findByName(eventName).orElseGet(() -> {
            return eventRepository.save(EventEntity.builder().name(eventName).build());
        });
    }
}
