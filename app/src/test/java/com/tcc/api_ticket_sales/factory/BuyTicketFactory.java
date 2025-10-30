package com.tcc.api_ticket_sales.factory;

import com.tcc.api_ticket_sales.application.dto.buy_ticket.BuyTicketRequestDTO;
import com.tcc.api_ticket_sales.application.dto.holder.HolderCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentResponseDTO;
import com.tcc.api_ticket_sales.application.dto.ticket.TicketCreateRequestDTO;
import org.instancio.Instancio;

import java.time.LocalDate;
import java.util.List;

public class BuyTicketFactory {
    public static BuyTicketRequestDTO createBuyTicketRequestDTO() {
        List<TicketCreateRequestDTO> tickets = createListTicketCreateRequestDTO();
        BuyTicketRequestDTO buyTicketRequestDTO = Instancio.create(BuyTicketRequestDTO.class);
        buyTicketRequestDTO.setTickets(tickets);

        return buyTicketRequestDTO;
    }

    public static BuyTicketRequestDTO createBuyTicketWithHolderDuplicate() {
        List<TicketCreateRequestDTO> tickets = createListTicketCreateRequestDTOWithHolderDuplicate();
        BuyTicketRequestDTO buyTicketRequestDTO = Instancio.create(BuyTicketRequestDTO.class);
        buyTicketRequestDTO.setTickets(tickets);

        return buyTicketRequestDTO;
    }


    public static PaymentResponseDTO createPaymentResponseDTO() {
        return Instancio.create(PaymentResponseDTO.class);
    }

    public static List<TicketCreateRequestDTO> createListTicketCreateRequestDTO() {
        return Instancio.ofList(TicketCreateRequestDTO.class).size(3).create();
    }

    public static List<TicketCreateRequestDTO> createListTicketCreateRequestDTOWithHolderDuplicate() {
        List<TicketCreateRequestDTO> tickets = createListTicketCreateRequestDTO();
        HolderCreateRequestDTO holder = new HolderCreateRequestDTO("teste", "teste@email", LocalDate.now());

        tickets.getFirst().getHolders().add(holder);
        tickets.getFirst().getHolders().add(holder);

        return tickets;
    }
}
