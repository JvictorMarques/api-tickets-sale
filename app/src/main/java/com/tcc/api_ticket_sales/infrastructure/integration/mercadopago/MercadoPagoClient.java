package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

import com.mercadopago.resources.payment.Payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Component
@Slf4j
public class MercadoPagoClient {

    @Value("${api.mercadopago-access-token}")
    private String accessToken;

    public void init () {
        MercadoPagoConfig.setAccessToken(accessToken);
        log.info("Iniciando MercadoPagoClient");
    }

    public Payment getPayment(Long paymentId) throws MPException, MPApiException {
        PaymentClient paymentClient = new PaymentClient();
        return paymentClient.get(paymentId);
    }

    public Payment createPayment(PaymentCreateRequest paymentCreateRequest) throws MPException, MPApiException {
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("x-idempotency-key", UUID.randomUUID().toString());
        MPRequestOptions requestOptions = MPRequestOptions.builder()
                .customHeaders(customHeaders)
                .build();


        PaymentClient paymentClient = new PaymentClient();
        Payment payment = paymentClient.create(paymentCreateRequest, requestOptions);

        log.info("Pagamento criado com sucesso, ID: {}", payment.getId());
        return payment;
    }
}
