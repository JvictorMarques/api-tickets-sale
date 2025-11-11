package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.gateways;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentResponseDTO;
import com.tcc.api_ticket_sales.domain.exception.BadGatewayException;
import com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.MercadoPagoClient;
import com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.MercadoPagoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MercadoPagoPaymentGatewayTest {

    @Mock
    private MercadoPagoMapper mercadoPagoMapper;

    @Mock
    private MercadoPagoClient mercadoPagoClient;

    @InjectMocks
    private MercadoPagoPaymentGateway mercadoPagoPaymentGateway;

    private PaymentRequestDTO paymentRequestDTO;
    private PaymentResponseDTO paymentResponseDTO;

    @BeforeEach
    void setUp() {
        paymentRequestDTO = new PaymentRequestDTO();
        paymentResponseDTO = new PaymentResponseDTO();
    }

    @Test
    @Tag("unit")
    void whenCreatePayment_thenSuccess() throws MPException, MPApiException {
        // Arrange
        when(mercadoPagoMapper.toPaymentCreateRequest(any()))
                .thenReturn(null);
        when(mercadoPagoClient.createPayment(any()))
                .thenReturn(null);
        doNothing().when(mercadoPagoClient).init();
        when(mercadoPagoMapper.toPaymentResponseDTO(any()))
                .thenReturn(paymentResponseDTO);

        // Act
        PaymentResponseDTO result = mercadoPagoPaymentGateway.createPayment(paymentRequestDTO);

        // Assert
        assertNotNull(result);
    }

    @Test
    @Tag("unit")
    void whenCreatePayment_thenThrowBusinessException() throws MPException, MPApiException {
        // Arrange
        when(mercadoPagoMapper.toPaymentCreateRequest(any()))
                .thenReturn(null);
        when(mercadoPagoClient.createPayment(any()))
                .thenThrow(new RuntimeException("MP Error"));

        // Act & Assert
        BadGatewayException exception = assertThrows(BadGatewayException.class, () ->
                mercadoPagoPaymentGateway.createPayment(paymentRequestDTO)
        );

        assertTrue(exception.getMessage().contains("Integração Mercado Pago"));
    }
}