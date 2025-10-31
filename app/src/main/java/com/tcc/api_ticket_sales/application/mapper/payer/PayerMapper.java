package com.tcc.api_ticket_sales.application.mapper.payer;

import com.tcc.api_ticket_sales.application.dto.buy_ticket.PayerRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PayerPaymentRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class PayerMapper {
    public PayerPaymentRequestDTO fromPayerRequestDTOToPayerPaymentRequestDTO(
            PayerRequestDTO dto) {
        return PayerPaymentRequestDTO.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }
}
