package com.tcc.api_ticket_sales.interfaces.controller;

import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeResponseDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeUpdateRequestDTO;
import com.tcc.api_ticket_sales.application.service.ticket_type.TicketTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ticket-type")
@Tag(name = "Ticket Type")
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    @PatchMapping("/{ticketTypeId}")
    public ResponseEntity<TicketTypeResponseDTO> update(@PathVariable UUID ticketTypeId, @Valid @RequestBody TicketTypeUpdateRequestDTO dto) {
        TicketTypeResponseDTO responseDTO = ticketTypeService.update(ticketTypeId, dto);
        return ResponseEntity.ok(responseDTO);
    }
}
