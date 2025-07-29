package com.tcc.api_ticket_sales.seeder;

import com.tcc.api_ticket_sales.entity.EventLocationEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.eventLocation.EventLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventLocationSeeder {

    private final EventLocationRepository eventLocationRepository;

    public void seed() {
        if(eventLocationRepository.count() > 0){
            return;
        }

        eventLocationRepository.save(EventLocationEntity.builder()
                .name("Domus Hall")
                .build()
        );
        eventLocationRepository.save(EventLocationEntity.builder()
                .name("Centro de Convenções")
                .build()
        );
        eventLocationRepository.save(EventLocationEntity.builder()
                .name("Teatro Pedra do Reino")
                .build()
        );
        eventLocationRepository.save(EventLocationEntity.builder()
                .name("Espaço Cultural")
                .build()
        );
    }

    public EventLocationEntity seedExists(String locationName){
        return eventLocationRepository.findByName(locationName).orElseGet(() -> {
            return eventLocationRepository.save(EventLocationEntity.builder().name(locationName).build());
        });
    }
}
