package com.tcc.api_ticket_sales.seeder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Profile("!test")
public class DataBaseSeeder implements CommandLineRunner {
    private final EventCategorySeeder eventCategorySeeder;
    private final EventLocationSeeder eventLocationSeeder;
    private final EventSeeder eventSeeder;
    private final TicketCategorySeeder ticketCategorySeeder;
    private final HolderSeeder holderSeeder;
    private final TicketSeeder ticketSeeder;

    @Override
    public void run(String... args){
        eventCategorySeeder.seed();
        eventLocationSeeder.seed();
        eventSeeder.seed();
        ticketCategorySeeder.seed();
        holderSeeder.seed();
        ticketSeeder.seed();
    }
}
