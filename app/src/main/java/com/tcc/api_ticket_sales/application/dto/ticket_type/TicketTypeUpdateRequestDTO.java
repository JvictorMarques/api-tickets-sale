package com.tcc.api_ticket_sales.application.dto.ticket_type;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketTypeUpdateRequestDTO {
    @Size(max = 100, message = "Nome do ingresso inválido")
    private String name;

    @Size(max = 100, message = "Descrição do ingresso inválido")
    private String description;

    @Positive(message = "Preço do ingresso precisa ser maior que 0")
    private BigDecimal price;

    @Positive(message = "Capacidade do ingresso precisa ser maior que 0")
    private Integer capacity;


    @FutureOrPresent(message = "A data/hora de venda do ingresso deve ser futura ou presente")
    private LocalDateTime dateInitial;

    private LocalDateTime dateFinal;
}
