package com.tcc.api_ticket_sales.seeder;

import com.tcc.api_ticket_sales.entity.EventCategoryEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.eventCategory.EventCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventCategorySeeder {

    private final EventCategoryRepository eventCategoryRepository;

    public void seed(){
        if(eventCategoryRepository.count() > 0) return;

        eventCategoryRepository.save(EventCategoryEntity.builder().name("Show").build());
        eventCategoryRepository.save(EventCategoryEntity.builder().name("Palestra").build());
        eventCategoryRepository.save(EventCategoryEntity.builder().name("Congresso").build());
        eventCategoryRepository.save(EventCategoryEntity.builder().name("Stand Up").build());
    }

    public EventCategoryEntity seedExists(String categoryName){
        return eventCategoryRepository.findByName(categoryName).orElseGet(() -> {
            return eventCategoryRepository.save(EventCategoryEntity.builder().name(categoryName).build());
        });
    }

}
