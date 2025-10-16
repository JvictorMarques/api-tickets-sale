package com.tcc.api_ticket_sales.interfaces.dto.ticket;

import com.tcc.api_ticket_sales.interfaces.dto.holder.HolderCreateRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class TicketCreateRequestDTO {
    private UUID id;
    private Integer quantity;
    private HolderCreateRequestDTO holder;
}
