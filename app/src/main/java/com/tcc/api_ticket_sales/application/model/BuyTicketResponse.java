package com.tcc.api_ticket_sales.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BuyTicketResponse {
    private String id;
    private String redirectUrl;
    private BigDecimal totalPrice;
}
