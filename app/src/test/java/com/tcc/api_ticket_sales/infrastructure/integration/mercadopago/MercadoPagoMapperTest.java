package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago;

import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.resources.payment.Payment;
import com.tcc.api_ticket_sales.application.dto.payment.ItemPaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PayerPaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentResponseDTO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MercadoPagoMapperTest {

    @Mock
    private Payment payment;

    @InjectMocks
    private MercadoPagoMapper mapper;

    @Test
    @Tag("unit")
    void whenMapToPaymentCreateRequest_thenSuccess() {
        // Arrange
        ItemPaymentRequestDTO item = ItemPaymentRequestDTO.builder()
                .id("ITEM_1")
                .title("Test Ticket")
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(100.00))
                .build();

        PayerPaymentRequestDTO payer = PayerPaymentRequestDTO.builder()
                .name("John Doe")
                .email("john@example.com")
                .build();

        PaymentRequestDTO paymentRequestDTO = PaymentRequestDTO.builder()
                .orderId("ORDER_123")
                .itemPaymentRequestDTOList(List.of(item))
                .payerPaymentRequestDTO(payer)
                .build();

        // Act
        PaymentCreateRequest result = mapper.toPaymentCreateRequest(paymentRequestDTO);
        BigDecimal amount = paymentRequestDTO.getItemPaymentRequestDTOList().stream().map(
                i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity()))
        ).reduce(BigDecimal.ZERO, BigDecimal::add);

        // Assert
        assertNotNull(result);
        assertEquals(paymentRequestDTO.getOrderId(), result.getExternalReference());
        assertEquals(paymentRequestDTO.getToken(), result.getToken());
        assertEquals(amount, result.getTransactionAmount());

        assertEquals(paymentRequestDTO.getPayerPaymentRequestDTO().getEmail(), result.getPayer().getEmail());
    }

    @Test
    void whenMapToPaymentResponseDTO_thenSuccess() {
        // Act
        PaymentResponseDTO result = mapper.toPaymentResponseDTO(payment);

        // Assert
        assertNotNull(result);
    }

    @Test
    @Tag("unit")
    void whenPreferenceHasNullFields_thenMapWithoutNullPointer() {
        // Arrange
        Payment nullPayment = new Payment();

        // Act
        PaymentResponseDTO result = mapper.toPaymentResponseDTO(nullPayment);

        // Assert
        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getStatus());
    }
}