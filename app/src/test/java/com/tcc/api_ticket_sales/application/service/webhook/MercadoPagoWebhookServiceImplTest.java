package com.tcc.api_ticket_sales.application.service.webhook;

import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhookPaymentDTO;
import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhoookRequestDTO;
import com.tcc.api_ticket_sales.domain.entity.PaymentEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.gateways.MercadoPagoNotificationGateway;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.tcc.api_ticket_sales.factory.MercadoPagoFactory.createMercadoPagoWebhookPaymentDTO;
import static com.tcc.api_ticket_sales.factory.MercadoPagoFactory.createMercadoPagoWebhoookRequestDTO;

import static com.tcc.api_ticket_sales.factory.PaymentFactory.createPaymentEntityWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MercadoPagoWebhookServiceImplTest {

    @Mock
    private MercadoPagoNotificationGateway mercadoPagoNotificationGateway;
    @Mock
    private MercadoPagoWebhookPaymentHandler handlerPayment;

    @InjectMocks
    private MercadoPagoWebhookServiceImpl mercadoPagoWebhookServiceImpl;

    @Tag("unit")
    @Test
    void processNotification_ShouldCallHandlerAndGet_WhenNotificationPayment() {
        MercadoPagoWebhoookRequestDTO mercadoPagoWebhoookRequestDTO = createMercadoPagoWebhoookRequestDTO();
        mercadoPagoWebhoookRequestDTO.setType("payment");
        MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO = createMercadoPagoWebhookPaymentDTO();
        PaymentEntity paymentEntity = createPaymentEntityWithId();

        when(mercadoPagoNotificationGateway.processNotificationPayment(any())).thenReturn(mercadoPagoWebhookPaymentDTO);
        when(handlerPayment.toPaymentEntity(any())).thenReturn(paymentEntity);
        doNothing().when(handlerPayment).handlePaymentEntity(any());

        mercadoPagoWebhookServiceImpl.processNotification(mercadoPagoWebhoookRequestDTO);

        verify(mercadoPagoNotificationGateway, times(1)).processNotificationPayment(any());
        verify(handlerPayment, times(1)).toPaymentEntity(any());
        verify(handlerPayment, times(1)).handlePaymentEntity(any());
    }

    @Tag("unit")
    @Test
    void processNotification_ShouldNoCallHandlerAndGet_WhenNotificationNoPayment() {
        MercadoPagoWebhoookRequestDTO mercadoPagoWebhoookRequestDTO = createMercadoPagoWebhoookRequestDTO();
        mercadoPagoWebhoookRequestDTO.setType("teste");

        mercadoPagoWebhookServiceImpl.processNotification(mercadoPagoWebhoookRequestDTO);

        verify(mercadoPagoNotificationGateway, times(0)).processNotificationPayment(any());
        verify(handlerPayment, times(0)).toPaymentEntity(any());
        verify(handlerPayment, times(0)).handlePaymentEntity(any());
    }
}