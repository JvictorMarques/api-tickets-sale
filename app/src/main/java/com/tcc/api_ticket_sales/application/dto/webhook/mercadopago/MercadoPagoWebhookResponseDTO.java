package com.tcc.api_ticket_sales.application.dto.webhook.mercadopago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MercadoPagoWebhookResponseDTO {
    boolean success;
    String updatedStatus;
}
