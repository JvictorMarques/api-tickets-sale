package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.gateways;

import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentResponseDTO;
import com.tcc.api_ticket_sales.domain.exception.BusinessException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MercadoPagoPaymentGatewayTest {

    @Mock
    private MercadoPagoMapper mercadoPagoMapper;

    @Mock
    private MercadoPagoClient mercadoPagoClient;

    @Mock
    private Preference preference;

    @InjectMocks
    private MercadoPagoPaymentGateway mercadoPagoPaymentGateway;

    private PaymentRequestDTO paymentRequestDTO;
    private PaymentResponseDTO paymentResponseDTO;

    @BeforeEach
    void setUp() {
        paymentRequestDTO = new PaymentRequestDTO();
        preference = new Preference();
        paymentResponseDTO = new PaymentResponseDTO();
    }

    @Test
    @Tag("unit")
    void whenCreatePreference_thenSuccess() throws MPException, MPApiException {
        // Arrange
        when(mercadoPagoMapper.toPreferenceRequest(any()))
                .thenReturn(null);
        when(mercadoPagoClient.createPreference(any()))
                .thenReturn(null);
        doNothing().when(mercadoPagoClient).init();
        when(mercadoPagoMapper.toPaymentResponseDTO(any()))
                .thenReturn(paymentResponseDTO);

        // Act
        PaymentResponseDTO result = mercadoPagoPaymentGateway.createPreference(paymentRequestDTO);

        // Assert
        assertNotNull(result);
    }

    @Test
    @Tag("unit")
    void whenCreatePreference_thenThrowBusinessException() throws MPException, MPApiException {
        // Arrange
        when(mercadoPagoMapper.toPreferenceRequest(any()))
                .thenReturn(null);
        when(mercadoPagoClient.createPreference(any()))
                .thenThrow(new RuntimeException("MP Error"));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
                mercadoPagoPaymentGateway.createPreference(paymentRequestDTO)
        );

        assertTrue(exception.getMessage().contains("erro no mercado pago"));
    }
}