package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago;


import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.resources.preference.PreferenceItem;
import com.tcc.api_ticket_sales.application.model.BuyTicketRequest;
import com.tcc.api_ticket_sales.application.model.BuyTicketResponse;
import com.tcc.api_ticket_sales.interfaces.dto.buy_ticket.BuyTicketResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MercadoPagoMapper {
    @Value("${api.mercadopago-notification-url}")
    private String notificationUrl;

    public PreferenceRequest toPreferenceRequest(BuyTicketRequest buyTicketRequest) {
        List<PreferenceItemRequest> items = buyTicketRequest.getTickets().stream()
                .map(ticket -> PreferenceItemRequest.builder()
                        .id(ticket.getId())
                        .title(ticket.getTitle())
                        .quantity(ticket.getQuantity())
                        .unitPrice(ticket.getUnitPrice())
                        .build()
                ).toList();

        PreferencePayerRequest payer = PreferencePayerRequest.builder()
                .name(buyTicketRequest.getPayer().getName())
                .email(buyTicketRequest.getPayer().getEmail())
                .build();


        return PreferenceRequest.builder()
                .items(items)
                .payer(payer)
                .notificationUrl(this.notificationUrl)
                .build();
    }

    public BuyTicketResponse toBuyTicketResponse(Preference preference){
        BigDecimal total = preference.getItems().stream().map(PreferenceItem::getUnitPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        return BuyTicketResponse.builder()
                .id(preference.getId())
                .redirectUrl(preference.getInitPoint())
                .totalPrice(total)
                .build();
    }
}
