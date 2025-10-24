package com.tcc.api_ticket_sales.application.dto.buy_ticket;

import com.tcc.api_ticket_sales.application.dto.ticket.TicketCreateRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class BuyTicketRequestDTO {
    @NotNull(message = "Tickets são obrigatórios.")
    @Size(min = 1, message = "Tickets não informados")
    @Valid
    private List<TicketCreateRequestDTO> tickets;

    @NotNull(message = "Pagador obrigatório")
    @Valid
    private PayerRequestDTO payer;
}
