package com.tcc.api_ticket_sales.interfaces.dto.buy_ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PayerRequestDTO {
    String name;
    String email;
}
