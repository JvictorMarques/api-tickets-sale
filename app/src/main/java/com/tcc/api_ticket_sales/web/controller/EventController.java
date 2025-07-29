package com.tcc.api_ticket_sales.web.controller;

import com.tcc.api_ticket_sales.application.service.EventService;
import com.tcc.api_ticket_sales.web.dto.BuyTicketRequestDTO;
import com.tcc.api_ticket_sales.web.dto.BuyTicketResponseDTO;
import com.tcc.api_ticket_sales.web.dto.EventResponseDTO;
import com.tcc.api_ticket_sales.web.dto.FilterEventRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/available")
    public ResponseEntity<List<EventResponseDTO>> getEventsAvailableByFilter(@ModelAttribute FilterEventRequestDTO filter){
        List<EventResponseDTO> events = eventService.getEventsAvailableByFilter(filter);

        if(events == null | events.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(events);
    }

    @PostMapping("/buy-ticket")
    public ResponseEntity<BuyTicketResponseDTO> buyTicket(@RequestBody BuyTicketRequestDTO requestDTO){
        BuyTicketResponseDTO buyId= eventService.buyTicket(requestDTO);

        return ResponseEntity.ok(buyId);
    }
}
