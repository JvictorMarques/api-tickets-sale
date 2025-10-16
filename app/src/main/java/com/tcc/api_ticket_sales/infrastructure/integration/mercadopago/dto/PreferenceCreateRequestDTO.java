package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PreferenceCreateRequestDTO {
    List<PreferenceItemCreateRequestDTO> items;
    PreferencePayerCreateRequestDTO payer;
    String notificationUrl;
}
