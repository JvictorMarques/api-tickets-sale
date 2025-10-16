package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PreferencePayerCreateRequestDTO {
    String name;
    String email;
}
