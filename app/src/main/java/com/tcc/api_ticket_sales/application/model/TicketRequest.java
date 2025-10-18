package com.tcc.api_ticket_sales.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TicketRequest {
    private String id;
    private String title;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
}
