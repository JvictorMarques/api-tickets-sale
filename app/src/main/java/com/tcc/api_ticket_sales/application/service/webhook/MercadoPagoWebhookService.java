package com.tcc.api_ticket_sales.application.service.webhook;
import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhoookRequestDTO;

public interface MercadoPagoWebhookService {
    void processNotification(MercadoPagoWebhoookRequestDTO requestDTO);
}
