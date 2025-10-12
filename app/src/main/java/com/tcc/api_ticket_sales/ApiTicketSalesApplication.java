package com.tcc.api_ticket_sales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ApiTicketSalesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiTicketSalesApplication.class, args);
	}

}
