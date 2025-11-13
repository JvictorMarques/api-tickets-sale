package com.tcc.api_ticket_sales.application.dto.event;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventUpdateRequestDTO {
    @Size(max = 100, message = "Nome do evento inválido")
    private String name;

    private String description;

    @Size(max = 100, message = "Nome da localização inválida")
    private String location;

    @Positive(message = "Capacidade do evento precisa ser maior que 0")
    private Integer capacity;

    @PositiveOrZero(message = "A idade de restrição não pode ser negativa")
    private Integer ageRestriction;

    @FutureOrPresent(message = "A data/hora do evento deve ser futura ou presente")
    private LocalDateTime dateInitial;

    @FutureOrPresent(message = "A data/hora do evento deve ser futura ou presente")
    private LocalDateTime dateFinal;
}
