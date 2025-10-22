package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago;


import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MercadoPagoMapper {
    @Value("${api.mercadopago-notification-url}")
    private String notificationUrl;

    public PreferenceRequest toPreferenceRequest(PaymentRequestDTO paymentRequestDTO) {
        List<PreferenceItemRequest> items = paymentRequestDTO.getItemPaymentRequestDTOList().stream()
                .map(item -> PreferenceItemRequest.builder()
                                    .id(item.getId())
                                    .title(item.getTitle())
                                    .quantity(item.getQuantity())
                                    .unitPrice(item.getUnitPrice())
                                    .build()
                ).toList();

        PreferencePayerRequest payer = PreferencePayerRequest.builder()
                .name(paymentRequestDTO.getPayerPaymentRequestDTO().getName())
                .email(paymentRequestDTO.getPayerPaymentRequestDTO().getEmail())
                .build();


        return PreferenceRequest.builder()
                .items(items)
                .payer(payer)
                .externalReference(paymentRequestDTO.getOrderId())
                .notificationUrl(this.notificationUrl)
                .build();
    }

    public PaymentResponseDTO toPaymentResponseDTO(Preference preference){
        return PaymentResponseDTO.builder()
                .id(preference.getId())
                .redirectUrl(preference.getInitPoint())
                .orderId(preference.getExternalReference())
                .build();
    }
}
