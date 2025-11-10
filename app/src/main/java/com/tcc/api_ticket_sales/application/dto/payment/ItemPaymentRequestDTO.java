package com.tcc.api_ticket_sales.application.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ItemPaymentRequestDTO {
    private String id;
    private String title;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
}
