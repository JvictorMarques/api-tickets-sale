package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.dto.PreferenceCreateRequestDTO;
import com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.dto.PreferenceCreateResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MercadoPagoClient {

    @Value("${api.mercadopago-access-token}")
    private String accessToken;
    @Value("${api.mercadopago-notification-url}")
    private String notificationUrl;

    public void init () {
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    public PreferenceCreateResponseDTO createPreference(
            PreferenceCreateRequestDTO preferenceCreateRequestDTO
    ) throws MPException, MPApiException {
        PreferenceClient preferenceClient = new PreferenceClient();

        List<PreferenceItemRequest> items = preferenceCreateRequestDTO.getItems().stream()
                .map(item -> PreferenceItemRequest.builder()
                        .id(item.getId())
                        .title(item.getTitle())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .build()
                ).toList();

        PreferencePayerRequest payer = PreferencePayerRequest.builder()
                .name(preferenceCreateRequestDTO.getPayer().getName())
                .email(preferenceCreateRequestDTO.getPayer().getEmail())
                .build();


        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .payer(payer)
                .notificationUrl(this.notificationUrl)
                .build();

        Preference preference = preferenceClient.create(preferenceRequest);

        return PreferenceCreateResponseDTO.builder()
                .id(preference.getId())
                .redirectUrl(preference.getInitPoint())
                .build();
    }
}
