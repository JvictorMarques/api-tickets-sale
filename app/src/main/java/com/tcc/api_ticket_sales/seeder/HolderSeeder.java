package com.tcc.api_ticket_sales.seeder;

import com.tcc.api_ticket_sales.entity.HolderEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.holder.HolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@RequiredArgsConstructor
@Component
public class HolderSeeder {
    private final HolderRepository holderRepository;

    public void seed() {
        if (holderRepository.count() > 0) return;

        holderRepository.save(HolderEntity.builder()
                .name("Titular 1")
                .cpf("231.650.450-70")
                .email("titular1@email.com")
                .bithDate(LocalDate.now().minusYears(20))
                .build()
        );

        holderRepository.save(HolderEntity.builder()
                .name("Titular 2")
                .cpf("751.200.310-21")
                .email("titular2@email.com")
                .bithDate(LocalDate.now().minusYears(30))
                .build()
        );
    }
}
