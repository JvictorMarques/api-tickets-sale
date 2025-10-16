package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class PreferenceItemCreateRequestDTO {
    private String id;
    private String title;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
}
