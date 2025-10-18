package com.tcc.api_ticket_sales.interfaces.dto.buy_ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class BuyTicketResponseDTO {
    private String orderId;
    private String redirectUrl;
}
