package com.tcc.api_ticket_sales.application.dto.event;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventCreateDTO {

    @NotBlank(message = "Nome do evento é obrigatório")
    private String name;

    private String description;

    @NotBlank(message = "Local do evento é obrigatório")
    private String location;

    @Positive(message = "Capacidade do evento precisa ser maior que 0")
    private int capacity;

    private int ageRestriction;

    @NotNull(message = "Data/hora inicial é obrigatório")
    @FutureOrPresent(message = "A data/hora do evento deve ser futura ou presente")
    private LocalDateTime dateInitial;

    @NotNull(message = "Data/hora final é obrigatório")
    @FutureOrPresent(message = "A data/hora do evento deve ser futura ou presente")
    private LocalDateTime dateFinal;
}
