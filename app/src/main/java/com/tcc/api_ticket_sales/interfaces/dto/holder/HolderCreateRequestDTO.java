package com.tcc.api_ticket_sales.interfaces.dto.holder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Data
public class HolderCreateRequestDTO {
    private String name;
    private String email;
    private LocalDate birthDate;
}
