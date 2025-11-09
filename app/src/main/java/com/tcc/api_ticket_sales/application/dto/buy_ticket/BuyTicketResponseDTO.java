package com.tcc.api_ticket_sales.application.dto.buy_ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class BuyTicketResponseDTO {
    private String status;
    private UUID orderId;
}
