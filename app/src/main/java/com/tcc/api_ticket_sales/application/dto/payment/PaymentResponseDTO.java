package com.tcc.api_ticket_sales.application.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentResponseDTO {
    private String id;
    private String status;
}
