package com.tcc.api_ticket_sales.infrastructure.seeder;

import com.tcc.api_ticket_sales.domain.entity.PaymentMethodEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentMethodRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentStatusRepository paymentStatusRepository;

    @Override
    public void run(String... args) {
        if(paymentMethodRepository.count() == 0){
            paymentMethodRepository.saveAll(List.of(
                    new PaymentMethodEntity("pix"),
                    new PaymentMethodEntity("card"),
                    new PaymentMethodEntity("ticket")
            ));
        }

        if(paymentStatusRepository.count() == 0){
            paymentStatusRepository.saveAll(List.of(
               new PaymentStatusEntity("pending"),
               new PaymentStatusEntity("approved"),
               new PaymentStatusEntity("rejected"),
               new PaymentStatusEntity("cancelled")
            ));
        }
    }
}
