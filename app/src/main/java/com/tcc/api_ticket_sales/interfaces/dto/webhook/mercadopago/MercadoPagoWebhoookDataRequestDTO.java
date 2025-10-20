package com.tcc.api_ticket_sales.interfaces.dto.webhook.mercadopago;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MercadoPagoWebhoookDataRequestDTO {
    @NotBlank
    private String id;
}
