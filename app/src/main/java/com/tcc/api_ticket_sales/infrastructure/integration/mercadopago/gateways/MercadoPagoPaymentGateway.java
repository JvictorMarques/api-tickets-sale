package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.gateways;

import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
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

    public PaymentResponseDTO createPreference(PaymentRequestDTO paymentRequestDTO){
        PreferenceRequest preferenceRequest = mercadoPagoMapper.toPreferenceRequest(paymentRequestDTO);

        try{
            mercadoPagoClient.init();
            Preference preference = mercadoPagoClient.createPreference(preferenceRequest);
            return mercadoPagoMapper.toPaymentResponseDTO(preference);
        }catch(Exception e) {
            throw  new BusinessException("erro no mercado pago: " + e.getMessage());
        }
    }
}
