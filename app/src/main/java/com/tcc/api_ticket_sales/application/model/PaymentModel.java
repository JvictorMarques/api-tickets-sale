package com.tcc.api_ticket_sales.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentModel {
    private BigDecimal amount;
    private String status;
    private String method;
    private String orderId;
}
