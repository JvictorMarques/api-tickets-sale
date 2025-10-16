package com.tcc.api_ticket_sales.interfaces.dto.ticket_type;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketTypeCreateRequestDTO {
    @NotBlank(message = "Nome do ingresso não informado")
    private String name;

    private String description;

    @NotNull(message = "Preço do ingresso obrigatório")
    @Positive(message = "Preço do ingresso precisa ser maior que 0")
    private BigDecimal price;

    @Positive(message = "Capacidade do ingresso precisa ser maior que 0")
    private int capacity;

    @NotNull(message = "Data/hora inicial é obrigatória")
    @FutureOrPresent(message = "A data/hora de venda do ingresso deve ser futura ou presente")
    private LocalDateTime dateInitial;

    private LocalDateTime dateFinal;
}
