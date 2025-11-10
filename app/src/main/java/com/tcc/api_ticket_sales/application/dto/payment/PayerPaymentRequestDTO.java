package com.tcc.api_ticket_sales.application.dto.payment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayerPaymentRequestDTO {
    private String name;
    private String email;
}
