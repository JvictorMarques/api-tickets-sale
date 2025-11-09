package com.tcc.api_ticket_sales.application.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaymentRequestDTO {
    private List<ItemPaymentRequestDTO> itemPaymentRequestDTOList;
    private PayerPaymentRequestDTO payerPaymentRequestDTO;
    private String orderId;
    private String token;
}
