package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago;

import com.tcc.api_ticket_sales.domain.interfaces.PaymentGateway;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.BuyTicketRequestDTO;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.BuyTicketResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class MercadoPagoPaymentGateway implements PaymentGateway {
    public BuyTicketResponseDTO createPayment (BuyTicketRequestDTO buyTicketRequestDTO){
        return new BuyTicketResponseDTO("", "");
    }
}
