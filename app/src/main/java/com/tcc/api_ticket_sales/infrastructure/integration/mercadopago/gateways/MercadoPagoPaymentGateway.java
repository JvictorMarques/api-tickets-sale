package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.gateways;

import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.tcc.api_ticket_sales.domain.exception.BadGatewayException;
import com.tcc.api_ticket_sales.domain.exception.BadRequestException;
import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import com.tcc.api_ticket_sales.domain.interfaces.PaymentGateway;
import com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.MercadoPagoClient;
import com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.MercadoPagoMapper;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MercadoPagoPaymentGateway implements PaymentGateway {

    private final MercadoPagoMapper mercadoPagoMapper;
    private final MercadoPagoClient mercadoPagoClient;

    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO){
        try{
            mercadoPagoClient.init();
            PaymentCreateRequest paymentCreateRequest = mercadoPagoMapper.toPaymentCreateRequest(paymentRequestDTO);
            Payment payment = mercadoPagoClient.createPayment(paymentCreateRequest);

            return mercadoPagoMapper.toPaymentResponseDTO(payment);
        }catch(Exception e) {
            throw  new BadGatewayException(String.format("Integração Mercado Pago: %s", e.getMessage()));
        }
    }
}
