package com.tcc.api_ticket_sales.interfaces.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class BuyTicketResponseDTO {
    private String id;
    private String redirectUrl;
}
