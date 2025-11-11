package com.tcc.api_ticket_sales.domain.interfaces;

import com.tcc.api_ticket_sales.application.dto.payment.PaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentResponseDTO;

public interface PaymentGateway {
    PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO);
}
