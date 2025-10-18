package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class MercadoPagoClient {

    @Value("${api.mercadopago-access-token}")
    private String accessToken;

    public void init () {
        MercadoPagoConfig.setAccessToken(accessToken);
        log.info("Iniciando MercadoPagoClient");
    }

    public Preference createPreference(
            PreferenceRequest preferenceRequest
    ) throws MPException, MPApiException {
        PreferenceClient preferenceClient = new PreferenceClient();

        return preferenceClient.create(preferenceRequest);
    }
}
