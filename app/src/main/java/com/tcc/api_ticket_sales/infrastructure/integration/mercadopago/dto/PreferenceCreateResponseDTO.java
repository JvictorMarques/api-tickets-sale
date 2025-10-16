package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PreferenceCreateResponseDTO {
    private String id;
    private String redirectUrl;
}
