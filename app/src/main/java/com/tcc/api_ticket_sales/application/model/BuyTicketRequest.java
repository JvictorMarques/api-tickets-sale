package com.tcc.api_ticket_sales.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyTicketRequest {
    private List<TicketRequest> tickets;
    private PayerRequest payer;
}
