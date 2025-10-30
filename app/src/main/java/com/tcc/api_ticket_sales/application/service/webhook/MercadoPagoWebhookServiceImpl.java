package com.tcc.api_ticket_sales.application.service.webhook;

import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhookPaymentDTO;
import com.tcc.api_ticket_sales.domain.entity.PaymentEntity;
import com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.gateways.MercadoPagoNotificationGateway;
import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhoookRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class MercadoPagoWebhookServiceImpl implements MercadoPagoWebhookService {

    private final MercadoPagoNotificationGateway mercadoPagoNotificationGateway;
    private final MercadoPagoWebhookPaymentHandler handlerPayment;

    @Transactional
    public void processNotification(MercadoPagoWebhoookRequestDTO requestDTO){
        log.info("MercadoPagoWebhookServiceImpl.processNotification", requestDTO.toString());

        if(requestDTO.getType().equalsIgnoreCase("payment")){
            MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO = mercadoPagoNotificationGateway.processNotificationPayment(requestDTO);
            PaymentEntity paymentEntity = handlerPayment.toPaymentEntity(mercadoPagoWebhookPaymentDTO);
            handlerPayment.handlePaymentEntity(paymentEntity);
        }else{
            log.info("MercadoPagoWebhookServiceImpl.processNotification", "Tipo de notificação não tratada: " + requestDTO.getType());
        }
    }
}
