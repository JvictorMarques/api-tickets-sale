package com.tcc.api_ticket_sales.application.mapper.payer;

import com.tcc.api_ticket_sales.application.dto.buy_ticket.BuyTicketRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PayerPaymentRequestDTO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.tcc.api_ticket_sales.factory.BuyTicketFactory.createBuyTicketRequestDTO;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PayerMapperTest {

    @InjectMocks
    private PayerMapper mapper;

    @Tag("unit")
    @Test
    void shouldMapPayerRequestDTOToPayerPaymentRequestDTO() {
        BuyTicketRequestDTO buyTicketRequestDTO = createBuyTicketRequestDTO();

        PayerPaymentRequestDTO paymentRequestDTO = mapper.fromPayerRequestDTOToPayerPaymentRequestDTO(
                buyTicketRequestDTO.getPayer());

        assertEquals(paymentRequestDTO.getName(), buyTicketRequestDTO.getPayer().getName());
        assertEquals(paymentRequestDTO.getEmail(), buyTicketRequestDTO.getPayer().getEmail());
    }
}