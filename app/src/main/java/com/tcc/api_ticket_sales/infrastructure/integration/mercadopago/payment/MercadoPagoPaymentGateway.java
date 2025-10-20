package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.payment;

import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.tcc.api_ticket_sales.application.model.BuyTicketRequest;
import com.tcc.api_ticket_sales.application.model.BuyTicketResponse;
import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import com.tcc.api_ticket_sales.domain.interfaces.PaymentGateway;
import com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.MercadoPagoClient;
import com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.MercadoPagoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MercadoPagoPaymentGateway implements PaymentGateway {

    private final MercadoPagoMapper mercadoPagoMapper;
    private final MercadoPagoClient mercadoPagoClient;

    public BuyTicketResponse createPreference(BuyTicketRequest buyTicketRequest){
        PreferenceRequest preferenceRequest = mercadoPagoMapper.toPreferenceRequest(buyTicketRequest);

        try{
            mercadoPagoClient.init();
            Preference preference = mercadoPagoClient.createPreference(preferenceRequest);
            return mercadoPagoMapper.toBuyTicketResponse(preference);
        }catch(Exception e) {
            throw  new BusinessException("erro no mercado pago: " + e.getMessage());
        }
    }
}
