package com.tcc.api_ticket_sales.interfaces.dto.buy_ticket;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PayerRequestDTO {
    @NotBlank(message = "Nome do pagador é obrigatório")
    private String name;

    @NotBlank(message = "Email do pagador é obrigatório")
    @Email(message = "Email inválido")
    private String email;
}
