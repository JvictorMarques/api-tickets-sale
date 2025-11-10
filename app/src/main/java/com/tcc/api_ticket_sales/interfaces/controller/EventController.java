package com.tcc.api_ticket_sales.interfaces.controller;

import com.tcc.api_ticket_sales.application.dto.event.EventFilterRequestDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventUpdateRequestDTO;
import com.tcc.api_ticket_sales.application.service.event.EventService;
import com.tcc.api_ticket_sales.application.service.ticket_type.TicketTypeService;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.interfaces.controller.exception.RestExceptionMessage;
import com.tcc.api_ticket_sales.application.dto.event.EventCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventResponseDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
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
    public ResponseEntity<EventResponseDTO> createEvent (@RequestBody @Valid EventCreateRequestDTO dto){
        EventResponseDTO event = eventService.create(dto);

        URI location = URI.create("/event/" + event.getId());
        return ResponseEntity.created(location).body(event);
    }

    @Operation(
            summary = "Editar evento"
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
                                            summary = "Evento não encontrado",
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
                                            name = "Evento existente",
                                            summary = "Evento existente",
                                            value = """
                                            {
                                                "message": "Evento existente.",
                                                "status": 409,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Evento existente."
                                                ]
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Capacidade menor que capacidade dos tipos de ingresso",
                                            summary = "Capacidade menor que capacidade dos tipos de ingresso",
                                            value = """
                                            {
                                                "message": "A capacidade do evento não pode ser menor que a soma das capacidades dos tipos de ingresso já cadastrados: [quantidade].",
                                                "status": 409,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "A capacidade do evento não pode ser menor que a soma das capacidades dos tipos de ingresso já cadastrados: [quantidade]."
                                                ]
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Restrição de idade não pode ser aumentada",
                                            summary = "Restrição de idade não pode ser aumentada",
                                            value = """
                                            {
                                                "message": "O evento possui ingressos já vendidos, portanto, a restrição de idade não pode ser aumentada.",
                                                "status": 409,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "O evento possui ingressos já vendidos, portanto, a restrição de idade não pode ser aumentada."
                                                ]
                                            }
                                            """
                                    ),

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
    @PatchMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> updateEvent(@RequestBody @Valid EventUpdateRequestDTO dto, @PathVariable UUID eventId){
        EventResponseDTO response = eventService.update(eventId, dto);

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evento excluído com sucesso"),
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
                                                "message": "Parâmetro 'EventId' inválido: valor '853f85c3-8af1-4dda-9032-3d3a0538b0b898' não pôde ser convertido para o tipo UUID.",
                                                "status": 400,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "capacity": "Parâmetro 'EventId' inválido: valor '853f85c3-8af1-4dda-9032-3d3a0538b0b898' não pôde ser convertido para o tipo UUID.",
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
                                            summary = "Evento não encontrado",
                                            value = """
                                            {
                                                "message": "Evento [id] não encontrado.",
                                                "status": 404,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Evento [id] não encontrado."
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
                                            name="Evento possui ingressos vendidos",
                                            summary = "Evento possui ingressos vendidos",
                                            value = """
                                            {
                                                "message": "O evento não pode ser deletado pois possui ingresso(s) vendido(s).",
                                                "status": 409,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "O evento não pode ser deletado pois possui ingresso(s) vendido(s)."
                                                ]
                                            }
                                            """
                                    ),
                            }

                    )
            ),
    })
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId){
        eventService.delete(eventId);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar eventos"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eventos listados com sucesso."),
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
    @GetMapping()
    public ResponseEntity<List<EventResponseDTO>> getEvents(EventFilterRequestDTO filter){
        List<EventResponseDTO> response = eventService.getByParams(filter);

        return ResponseEntity.ok().body(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento retornado com sucesso"),
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
                                                "message": "Parâmetro 'eventId' inválido: valor [eventId] não pôde ser convertido para o tipo UUID.",
                                                "status": 400,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "capacity": "Parâmetro 'EventId' inválido: valor [eventId] não pôde ser convertido para o tipo UUID.",
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
                                            summary = "Evento não encontrado",
                                            value = """
                                            {
                                                "message": "Evento [id] não encontrado.",
                                                "status": 404,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Evento [id] não encontrado."
                                                ]
                                            }
                                            """
                                    ),
                            }
                    )
            ),
    })
    @GetMapping("{eventId}")
    public ResponseEntity<EventResponseDTO> getById(@PathVariable UUID eventId){
        EventResponseDTO eventResponseDTO = eventService.getById(eventId);

        return ResponseEntity.ok(eventResponseDTO);
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
    public ResponseEntity<TicketTypeResponseDTO> createTicketType(@RequestBody @Valid TicketTypeCreateRequestDTO dto, @PathVariable UUID eventId){
        TicketTypeResponseDTO ticket = ticketTypeService.create(eventId, dto);

        URI location = URI.create("/ticket-type/" + ticket.getId());
        return ResponseEntity.created(location).body(ticket);
    }
}
