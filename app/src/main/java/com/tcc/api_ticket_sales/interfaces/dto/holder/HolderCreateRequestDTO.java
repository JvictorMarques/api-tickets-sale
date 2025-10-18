package com.tcc.api_ticket_sales.interfaces.dto.holder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Data
public class HolderCreateRequestDTO {
    @NotBlank(message = "Nome do titular é obrigatório.")
    private String name;

    @NotBlank(message = "Email do titular é obrigatório.")
    @Email(message = "Email inválido")
    private String email;

    @NotNull(message = "Data de aniversário do titular é obrigatório.")
    private LocalDate birthDate;
}
