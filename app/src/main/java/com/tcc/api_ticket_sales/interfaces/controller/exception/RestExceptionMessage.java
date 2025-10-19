package com.tcc.api_ticket_sales.interfaces.controller.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo padrão de erro retornado pela API")
public class RestExceptionMessage {

    @Schema(description = "Mensagem descritiva do erro")
    private String message;

    @Schema(description = "Código HTTP do erro")
    private int status;

    @Schema(description = "Data e hora do erro")
    private LocalDateTime timeStamp;

    @Schema(description = "Lista de erros de validação (se houver), se não houver exibirá a mensagem")
    private List<String> errors = List.of();
}
