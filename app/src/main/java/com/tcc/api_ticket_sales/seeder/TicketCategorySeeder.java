package com.tcc.api_ticket_sales.seeder;

import com.tcc.api_ticket_sales.entity.TicketCategoryEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.ticketCategory.TicketCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class TicketCategorySeeder {
    private final TicketCategoryRepository ticketCategoryRepository;

    public void seed() {
        if (ticketCategoryRepository.count() > 0) {
            return;
        }

        ticketCategoryRepository.save(TicketCategoryEntity.builder()
                .name("Inteira")
                .build()
        );
        ticketCategoryRepository.save(TicketCategoryEntity.builder()
                .name("Meia-Entrada")
                .build()
        );
        ticketCategoryRepository.save(TicketCategoryEntity.builder()
                .name("VIP")
                .build()
        );
    }

    public TicketCategoryEntity seedExists(String categoryName){
        return ticketCategoryRepository.findByName(categoryName).orElseGet(() -> {
            return ticketCategoryRepository.save(TicketCategoryEntity.builder().name(categoryName).build());
        });
    }
}
