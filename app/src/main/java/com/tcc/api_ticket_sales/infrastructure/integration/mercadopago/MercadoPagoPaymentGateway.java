package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago;

import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.tcc.api_ticket_sales.application.model.BuyTicketRequest;
import com.tcc.api_ticket_sales.application.model.BuyTicketResponse;
import com.tcc.api_ticket_sales.domain.interfaces.PaymentGateway;
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
            e.printStackTrace();
        }
        return null;
    }
}
