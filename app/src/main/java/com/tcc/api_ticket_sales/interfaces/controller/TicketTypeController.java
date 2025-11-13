package com.tcc.api_ticket_sales.interfaces.controller;

import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeResponseDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeUpdateRequestDTO;
import com.tcc.api_ticket_sales.application.service.ticket_type.TicketTypeService;
import com.tcc.api_ticket_sales.interfaces.controller.exception.RestExceptionMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @Operation(
            summary = "Editar tipo de ingresso"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de ingresso editado com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Formato de requisição inválido e validações dos campos",
                    content = @Content(
                            schema = @Schema(implementation = RestExceptionMessage.class),
                            examples= {
                                    @ExampleObject(
                                            summary = "Validação de campo",
                                            value = """
                                            {
                                                "message": "Erro de validação",
                                                "status": 400,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "capacity": "A capacidade deve ser um número positivo.",
                                                ]
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recursos não encontrados",
                    content = @Content(
                            schema = @Schema(implementation = RestExceptionMessage.class),
                            examples = {
                                    @ExampleObject(
                                            summary = "Tipo de ingresso não encontrado",
                                            value = """
                                            {
                                                "message": "Tipo de ingresso [id] não encontrado.",
                                                "status": 404,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Tipo de ingresso [id] não encontrado."
                                                ]
                                            }
                                            """
                                    ),
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflitos",
                    content = @Content(
                            schema = @Schema(implementation = RestExceptionMessage.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Capacidade menor que ingressos vendidos",
                                            summary = "Capacidade menor que ingressos vendidos",
                                            value = """
                                            {
                                                "message": "Capacidade inválida para atualização: existem [quantidade] ingressos já vendidos.",
                                                "status": 409,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Capacidade inválida para atualização: existem [quantidade] ingressos já vendidos."
                                                ]
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name="Capacidade excede a capacidade do evento",
                                            summary = "Capacidade excede a capacidade do evento",
                                            value = """
                                            {
                                                "message": "Capacidade insuficiente: apenas 3 vagas restantes no evento para novos tipos de ingressos.",
                                                "status": 409,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Capacidade insuficiente: apenas 3 vagas restantes no evento para novos tipos de ingressos."
                                                ]
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "A data de venda do tipo de ingresso não pode ocorrer após a data do evento",
                                            summary = "A data de venda do tipo de ingresso não pode ocorrer após a data do evento",
                                            value = """
                                            {
                                                "message": "A data de venda dos ingressos não pode ser após o evento.",
                                                "status": 409,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "A data de venda dos ingressos não pode ser após o evento."
                                                ]
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "O evento já possui um tipo de ingresso com as mesmas características",
                                            summary = "O evento já possui um tipo de ingresso com as mesmas características",
                                            value = """
                                            {
                                                "message": "Tipo de ingresso duplicado: este evento já possui o tipo de ingresso informado.",
                                                "status": 409,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Tipo de ingresso duplicado: este evento já possui o tipo de ingresso informado."
                                                ]
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Tipo de ingresso encerrado",
                                            summary = "Tipo de ingresso encerrado",
                                            value = """
                                            {
                                                "message": "Tipo de ingresso encerrado.",
                                                "status": 409,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Tipo de ingresso encerrado."
                                                ]
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Evento encerrado",
                                            summary = "Evento encerrado",
                                            value = """
                                            {
                                                "message": "Evento encerrado.",
                                                "status": 409,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Evento encerrado."
                                                ]
                                            }
                                            """
                                    )
                            }

                    )
            ),
    })
    @PatchMapping("/{ticketTypeId}")
    public ResponseEntity<TicketTypeResponseDTO> update(@PathVariable UUID ticketTypeId, @Valid @RequestBody TicketTypeUpdateRequestDTO dto) {
        TicketTypeResponseDTO responseDTO = ticketTypeService.update(ticketTypeId, dto);
        return ResponseEntity.ok(responseDTO);
    }


    @Operation(
            summary = "Excluir tipo de ingresso"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de ingresso excluído com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Formato de requisição inválido e validações dos campos",
                    content = @Content(
                            schema = @Schema(implementation = RestExceptionMessage.class),
                            examples= {
                                    @ExampleObject(
                                            summary = "Parâmetro inválido",
                                            value = """
                                            {
                                                "message": "Parâmetro 'ticketTypeId' inválido: valor '853f85c3-8af1-4dda-9032-3d3a0538b0b898' não pôde ser convertido para o tipo UUID.",
                                                "status": 400,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "capacity": "Parâmetro 'ticketTypeId' inválido: valor '853f85c3-8af1-4dda-9032-3d3a0538b0b898' não pôde ser convertido para o tipo UUID.",
                                                ]
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recursos não encontrados",
                    content = @Content(
                            schema = @Schema(implementation = RestExceptionMessage.class),
                            examples = {
                                    @ExampleObject(
                                            summary = "Tipo de ingresso não encontrado",
                                            value = """
                                            {
                                                "message": "Tipo de ingresso [id] não encontrado.",
                                                "status": 404,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Tipo de ingresso [id] não encontrado."
                                                ]
                                            }
                                            """
                                    ),
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflitos",
                    content = @Content(
                            schema = @Schema(implementation = RestExceptionMessage.class),
                            examples = {
                                    @ExampleObject(
                                            name="Tipo de ingresso possui ingressos vendidos",
                                            summary = "Tipo de ingresso possui ingressos vendidos",
                                            value = """
                                            {
                                                "message": "Tipo de ingresso não pode ser deletado pois possui ingresso(s) vendido(s).",
                                                "status": 409,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Tipo de ingresso não pode ser deletado pois possui ingresso(s) vendido(s)."
                                                ]
                                            }
                                            """
                                    ),
                            }

                    )
            ),
    })
    @DeleteMapping("/{ticketTypeId}")
    public ResponseEntity<Void> delete(@PathVariable UUID ticketTypeId){
        ticketTypeService.delete(ticketTypeId);
        return ResponseEntity.noContent().build();
    }
}
