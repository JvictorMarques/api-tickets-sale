package com.tcc.api_ticket_sales.domain.model;

import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class TicketBuyModel {
    TicketTypeEntity ticketTypeEntity;
    int quantity;
}
