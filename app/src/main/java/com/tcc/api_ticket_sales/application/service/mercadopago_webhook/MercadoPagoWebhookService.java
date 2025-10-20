package com.tcc.api_ticket_sales.application.service.mercadopago_webhook;
import com.tcc.api_ticket_sales.interfaces.dto.webhook.mercadopago.MercadoPagoWebhoookRequestDTO;

public interface MercadoPagoWebhookService {
    void processNotification(MercadoPagoWebhoookRequestDTO requestDTO);
}
