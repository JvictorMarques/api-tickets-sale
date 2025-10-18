package com.tcc.api_ticket_sales.domain.interfaces;

import com.tcc.api_ticket_sales.application.model.BuyTicketRequest;
import com.tcc.api_ticket_sales.application.model.BuyTicketResponse;

public interface PaymentGateway {
    BuyTicketResponse createPreference(BuyTicketRequest buyTicketRequest);
}
