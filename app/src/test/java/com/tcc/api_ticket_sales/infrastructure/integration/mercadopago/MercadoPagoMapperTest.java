package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago;

import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.tcc.api_ticket_sales.application.dto.payment.ItemPaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PayerPaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MercadoPagoMapperTest {

    @Mock
    private Preference  preference;

    @InjectMocks
    private MercadoPagoMapper mapper;

    private PaymentRequestDTO paymentRequestDTO;
    private static final String NOTIFICATION_URL = "http://test-notification.com";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mapper, "notificationUrl", NOTIFICATION_URL);

        // Setup PaymentRequestDTO
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

        paymentRequestDTO = PaymentRequestDTO.builder()
                .orderId("ORDER_123")
                .itemPaymentRequestDTOList(List.of(item))
                .payerPaymentRequestDTO(payer)
                .build();
    }

    @Test
    @Tag("unit")
    void whenMapToPreferenceRequest_thenSuccess() {
        // Act
        PreferenceRequest result = mapper.toPreferenceRequest(paymentRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(paymentRequestDTO.getOrderId(), result.getExternalReference());
        assertEquals(NOTIFICATION_URL, result.getNotificationUrl());

        // Validate items
        List<PreferenceItemRequest> items = result.getItems();
        assertNotNull(items);
        assertEquals(1, items.size());

        PreferenceItemRequest mappedItem = items.getFirst();
        ItemPaymentRequestDTO originalItem = paymentRequestDTO.getItemPaymentRequestDTOList().getFirst();
        assertEquals(originalItem.getId(), mappedItem.getId());
        assertEquals(originalItem.getTitle(), mappedItem.getTitle());
        assertEquals(originalItem.getQuantity(), mappedItem.getQuantity());
        assertEquals(originalItem.getUnitPrice(), mappedItem.getUnitPrice());

        // Validate payer
        PreferencePayerRequest mappedPayer = result.getPayer();
        assertNotNull(mappedPayer);
        assertEquals(paymentRequestDTO.getPayerPaymentRequestDTO().getName(), mappedPayer.getName());
        assertEquals(paymentRequestDTO.getPayerPaymentRequestDTO().getEmail(), mappedPayer.getEmail());
    }

//    @Test
//    void whenMapToPaymentResponseDTO_thenSuccess() {
//        // Act
//        PaymentResponseDTO result = mapper.toPaymentResponseDTO(preference);
//
//        String id = "id_test";
//        String initPoint = "init_point_test";
//        String externalReference = "external_reference_test";
//
//        when(preference.getId()).thenReturn(id);
//        when(preference.getInitPoint()).thenReturn(initPoint);
//        when(preference.getExternalReference()).thenReturn(externalReference);
//
//        // Assert
//        assertNotNull(result);
//    }

    @Test
    @Tag("unit")
    void whenMapToPreferenceRequestWithMultipleItems_thenSuccess() {
        // Arrange
        ItemPaymentRequestDTO item1 = ItemPaymentRequestDTO.builder()
                .id("ITEM_1")
                .title("Ticket 1")
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(50.00))
                .build();

        ItemPaymentRequestDTO item2 = ItemPaymentRequestDTO.builder()
                .id("ITEM_2")
                .title("Ticket 2")
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(75.00))
                .build();

        paymentRequestDTO.setItemPaymentRequestDTOList(List.of(item1, item2));

        // Act
        PreferenceRequest result = mapper.toPreferenceRequest(paymentRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getItems().size());
        assertEquals("Ticket 1", result.getItems().get(0).getTitle());
        assertEquals("Ticket 2", result.getItems().get(1).getTitle());
    }

    @Test
    @Tag("unit")
    void whenPreferenceHasNullFields_thenMapWithoutNullPointer() {
        // Arrange
        Preference nullPreference = new Preference();

        // Act
        PaymentResponseDTO result = mapper.toPaymentResponseDTO(nullPreference);

        // Assert
        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getRedirectUrl());
        assertNull(result.getOrderId());
    }
}