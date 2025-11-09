package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago;


import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.resources.payment.Payment;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MercadoPagoMapper {
    public PaymentResponseDTO toPaymentResponseDTO(Payment payment){
        return PaymentResponseDTO.builder()
                .id(payment.getId() != null ? payment.getId().toString() : null)
                .status(payment.getStatus())
                .build();
    }

    public PaymentCreateRequest toPaymentCreateRequest(PaymentRequestDTO paymentRequestDTO){
        BigDecimal amount = paymentRequestDTO.getItemPaymentRequestDTOList().stream().map(
                item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        ).reduce(BigDecimal.ZERO, BigDecimal::add);

        return PaymentCreateRequest.builder()
                .installments(1)
                .transactionAmount(amount)
                .description("Simulação de pagamento backend")
                .token(paymentRequestDTO.getToken())
                .payer(PaymentPayerRequest.builder()
                        .email(paymentRequestDTO.getPayerPaymentRequestDTO().getEmail())
                        .build()
                )
                .externalReference(paymentRequestDTO.getOrderId())
                .build();
    }
}
