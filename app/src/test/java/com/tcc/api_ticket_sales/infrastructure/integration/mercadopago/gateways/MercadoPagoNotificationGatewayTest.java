package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.gateways;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhookDataRequestDTO;
import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhookPaymentDTO;
import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhoookRequestDTO;
import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.MercadoPagoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MercadoPagoNotificationGatewayTest {

    @Mock
    private MercadoPagoClient mercadoPagoClient;
    @Mock
    private Payment payment;


    @InjectMocks
    private MercadoPagoNotificationGateway mercadoPagoNotificationGateway;

    private MercadoPagoWebhoookRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new MercadoPagoWebhoookRequestDTO();
        MercadoPagoWebhookDataRequestDTO data = new MercadoPagoWebhookDataRequestDTO();
        data.setId("123");
        requestDTO.setData(data);
    }

    @Test
    @Tag("unit")
    void whenProcessNotification_thenSuccess() throws MPException, MPApiException {
        // Arrange
        when(mercadoPagoClient.getPayment(any())).thenReturn(payment);
        String paymentStatus = "payment";
        String paymentMethod = "credit_card";
        String orderId = "order_teste";
        BigDecimal amount = new BigDecimal(100);

        when(payment.getStatus()).thenReturn(paymentStatus);
        when(payment.getPaymentTypeId()).thenReturn(paymentMethod);
        when(payment.getExternalReference()).thenReturn(orderId);
        when(payment.getTransactionAmount()).thenReturn(amount);

        // Act
        MercadoPagoWebhookPaymentDTO result = mercadoPagoNotificationGateway
                .processNotificationPayment(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(paymentStatus, result.getStatus());
        assertEquals(paymentMethod, result.getMethod());
        assertEquals(orderId, result.getOrderId());
        assertEquals(amount, result.getAmount());

    }

    @Test
    @Tag("unit")
    void whenPaymentNotFound_thenThrowBusinessException() throws MPException, MPApiException {
        // Arrange
        when(mercadoPagoClient.getPayment(any())).thenReturn(null);

        // Act & Assert
        assertThrows(
                BusinessException.class,
                () -> mercadoPagoNotificationGateway.processNotificationPayment(requestDTO)
        );
    }

    @Test
    @Tag("unit")
    void whenClientThrowsException_thenThrowBusinessException() throws MPException, MPApiException {
        // Arrange
        when(mercadoPagoClient.getPayment(any()))
                .thenThrow(new RuntimeException("MP Error"));

        // Act & Assert
        assertThrows(
                BusinessException.class,
                () -> mercadoPagoNotificationGateway.processNotificationPayment(requestDTO)
        );
    }
}