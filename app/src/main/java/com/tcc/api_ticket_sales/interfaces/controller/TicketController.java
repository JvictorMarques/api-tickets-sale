package com.tcc.api_ticket_sales.interfaces.controller;

import com.tcc.api_ticket_sales.application.service.buy_ticket.BuyTicketService;
import com.tcc.api_ticket_sales.interfaces.dto.buy_ticket.BuyTicketRequestDTO;
import com.tcc.api_ticket_sales.interfaces.dto.buy_ticket.BuyTicketResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ticket")
@Tag(name = "Ticket")
public class TicketController {

    private final BuyTicketService buyTicketService;


    @PostMapping("buy")
    public ResponseEntity<BuyTicketResponseDTO> buyTicket(@RequestBody  @Valid BuyTicketRequestDTO  buyTicketRequestDTO) {
        BuyTicketResponseDTO response = buyTicketService.buyTickets(buyTicketRequestDTO);

        return ResponseEntity.ok().body(response);
    }
}
