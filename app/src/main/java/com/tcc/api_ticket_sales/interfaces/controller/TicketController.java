package com.tcc.api_ticket_sales.interfaces.controller;

import com.tcc.api_ticket_sales.application.service.ticket.TicketService;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.TicketCreateRequestDTO;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.TicketCreateResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/event/{eventId}/ticket")
    public ResponseEntity<TicketCreateResponseDTO> createTicket(@RequestBody @Valid TicketCreateRequestDTO dto, @PathVariable UUID eventId){
        TicketCreateResponseDTO ticket = ticketService.create(eventId, dto);

        URI location = URI.create("/event/" + eventId + ticket.getId());
        return ResponseEntity.created(location).body(ticket);
    }
}
