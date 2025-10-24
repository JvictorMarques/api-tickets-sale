package com.tcc.api_ticket_sales.application.dto.ticket;

import com.tcc.api_ticket_sales.application.dto.holder.HolderCreateRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class TicketCreateRequestDTO {
    @NotNull(message = "Id do ingresso é obrigatório.")
    private UUID id;

    @NotNull
    @Size(min = 1, message = "Titulares são obrigatórios")
    @Valid
    private List<HolderCreateRequestDTO> holders;
}
