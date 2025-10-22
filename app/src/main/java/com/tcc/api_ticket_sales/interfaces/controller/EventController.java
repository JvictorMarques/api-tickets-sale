package com.tcc.api_ticket_sales.interfaces.controller;

import com.tcc.api_ticket_sales.application.service.event.EventService;
import com.tcc.api_ticket_sales.application.service.ticket_type.TicketTypeService;
import com.tcc.api_ticket_sales.interfaces.controller.exception.RestExceptionMessage;
import com.tcc.api_ticket_sales.application.dto.event.EventCreateDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventResponseDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateResponseDTO;
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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
@Tag(name = "Event")
public class EventController {

    private final EventService eventService;
    private final TicketTypeService ticketTypeService;

    @Operation(
            summary = "Cadastrar Evento"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento criado com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Formato de requisição inválido e validações dos campos",
                    content = @Content(
                            schema = @Schema(implementation = RestExceptionMessage.class),
                            examples= {
                                    @ExampleObject(
                                            name = "Json malformado",
                                            summary = "Json malformado",
                                            value = """
                                            {
                                                "message": "Formato de data inválido. Use o padrão yyyy-MM-dd'T'HH:mm:ss",
                                                "status": 400,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Formato de data/hora inválido para o campo 'saleStartDate'. Valor recebido: '13/10/2025 18:00'. Formato esperado: 'yyyy-MM-dd'T'HH:mm:ss'."
                                                ]
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Validação de campo",
                                            summary = "Validação de campo",
                                            value = """
                                            {
                                                "message": "Erro de validação",
                                                "status": 400,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "dateInitial": "Data/hora inicial é obrigatória"
                                                ]
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflitos",
                    content = @Content(
                            schema = @Schema(implementation = RestExceptionMessage.class),
                            examples= @ExampleObject(
                                    summary = "Evento existente",
                                    value = """
                                            {
                                                "message": "Evento existente.",
                                                "status": 409,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Evento existente.a"
                                                ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Violação da regra de negócio",
                    content = @Content(
                            schema = @Schema(implementation = RestExceptionMessage.class),
                            examples= @ExampleObject(
                                    summary = "Data inicial maior que data final.",
                                    value = """
                                            {
                                                "message": "Data inicial maior que data final.",
                                                "status": 422,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Data inicial maior que data final."
                                                ]
                                            }
                                            """
                            )
                    )
            ),
    })
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent (@RequestBody @Valid EventCreateDTO dto){
        EventResponseDTO event = eventService.createEvent(dto);

        URI location = URI.create("/event/" + event.getId());
        return ResponseEntity.created(location).body(event);
    }

    @Operation(
            summary = "Cadastrar ingresso a um evento"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de ingresso cadastrado com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Formato de requisição inválido e validações dos campos",
                    content = @Content(
                            schema = @Schema(implementation = RestExceptionMessage.class),
                            examples= {
                                    @ExampleObject(
                                            name = "Json malformado",
                                            summary = "Json malformado",
                                            value = """
                                            {
                                                "message": "Formato de data inválido. Use o padrão yyyy-MM-dd'T'HH:mm:ss",
                                                "status": 400,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Formato de data/hora inválido para o campo 'saleStartDate'. Valor recebido: '13/10/2025 18:00'. Formato esperado: 'yyyy-MM-dd'T'HH:mm:ss'."
                                                ]
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Validação de campo",
                                            summary = "Validação de campo",
                                            value = """
                                            {
                                                "message": "Erro de validação",
                                                "status": 400,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "dateInitial": "Data/hora inicial é obrigatória"
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
                                            summary = "Evento não possue registro",
                                            value = """
                                            {
                                                "message": "Evento não encontrado",
                                                "status": 404,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Evento não encontrado."
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
                                    ),
                                    @ExampleObject(
                                            name="Evento não possui vagas suficientes para novos tipos de ingressos",
                                            summary = "Evento não possui vagas suficientes para novos tipos de ingressos",
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
                                    )
                            }

                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Violação da regra de negócio",
                    content = @Content(
                            schema = @Schema(implementation = RestExceptionMessage.class),
                            examples= @ExampleObject(
                                    summary = "Data inicial maior que data final.",
                                    value = """
                                            {
                                                "message": "Data inicial maior que data final.",
                                                "status": 422,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Data inicial maior que data final."
                                                ]
                                            }
                                            """
                            )
                    )
            ),
    })
    @PostMapping("/{eventId}/ticket-type")
    public ResponseEntity<TicketTypeCreateResponseDTO> createTicketType(@RequestBody @Valid TicketTypeCreateRequestDTO dto, @PathVariable UUID eventId){
        TicketTypeCreateResponseDTO ticket = ticketTypeService.create(eventId, dto);

        URI location = URI.create("/ticket-type/" + ticket.getId());
        return ResponseEntity.created(location).body(ticket);
    }
}
