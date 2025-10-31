package com.tcc.api_ticket_sales.factory;

import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhookPaymentDTO;
import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhoookRequestDTO;
import org.instancio.Instancio;

import java.util.UUID;

public class MercadoPagoFactory {

    public static MercadoPagoWebhookPaymentDTO createMercadoPagoWebhookPaymentDTO(){
        MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO = Instancio.create(MercadoPagoWebhookPaymentDTO.class);
        mercadoPagoWebhookPaymentDTO.setOrderId(UUID.randomUUID().toString());
        return mercadoPagoWebhookPaymentDTO;
    }

    public static MercadoPagoWebhoookRequestDTO createMercadoPagoWebhoookRequestDTO(){
        return Instancio.create(MercadoPagoWebhoookRequestDTO.class);
    }
}
