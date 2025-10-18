package com.tcc.api_ticket_sales.interfaces.mapper.payer;

import com.tcc.api_ticket_sales.application.model.PayerRequest;
import com.tcc.api_ticket_sales.interfaces.dto.buy_ticket.PayerRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class PayerMapper {

    public PayerRequest fromPayerRequestDTOToPayerRequest(PayerRequestDTO payerRequestDTO) {
        return new PayerRequest(
                payerRequestDTO.getName(),
                payerRequestDTO.getEmail()
        );
    }
}
