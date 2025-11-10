package com.tcc.api_ticket_sales.interfaces.controller;

import com.tcc.api_ticket_sales.application.service.buy_ticket.BuyTicketService;
import com.tcc.api_ticket_sales.application.dto.buy_ticket.BuyTicketRequestDTO;
import com.tcc.api_ticket_sales.application.dto.buy_ticket.BuyTicketResponseDTO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ticket")
@Tag(name = "Ticket")
public class TicketController {

    private final BuyTicketService buyTicketService;


    @Operation(
            summary = "Comprar ingressos"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido realizado com sucesso"),
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
                                            name = "Quantidade de ingressos da compra excede a quantidade disponível",
                                            summary = "Quantidade de ingressos da compra excede a quantidade disponível",
                                            value = """
                                            {
                                                "message": "A quantidade de ingressos da compra excede a quantidade disponível.",
                                                "status": 409,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "A quantidade de ingressos da compra excede a quantidade disponível."
                                                ]
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name="Titular já possui ingresso para o evento",
                                            summary = "Titular já possui ingresso para o evento",
                                            value = """
                                            {
                                                "message": "O titular [nome] já possui ingresso para o evento",
                                                "status": 409,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "O titular [nome] já possui ingresso para o evento"
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
            @ApiResponse(
                    responseCode = "422",
                    description = "Violação da regra de negócio",
                    content = @Content(
                            schema = @Schema(implementation = RestExceptionMessage.class),
                            examples= {
                                    @ExampleObject(
                                    summary = "Titular duplicado para o mesmo ingresso na compra",
                                    name = "Titular duplicado para o mesmo ingresso na compra",
                                    value = """
                                            {
                                                "message": "Não é possível comprar mais de um ingresso para o titular: [nome].",
                                                "status": 422,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Não é possível comprar mais de um ingresso para o titular: [nome]."
                                                ]
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            summary = "Titular não possui a idade mínima para o evento",
                                            name = "Titular não possui a idade mínima para o evento",
                                            value = """
                                            {
                                                "message": "O titular [nome] não possue a idade necessária para o evento.",
                                                "status": 422,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "O titular [nome] não possue a idade necessária para o evento."
                                                ]
                                            }
                                            """
                                    ),
                            }
                    )
            ),
    })
    @PostMapping("buy")
    public ResponseEntity<BuyTicketResponseDTO> buyTicket(@RequestBody  @Valid BuyTicketRequestDTO  buyTicketRequestDTO) {
        BuyTicketResponseDTO response = buyTicketService.buyTickets(buyTicketRequestDTO);

        return ResponseEntity.ok().body(response);
    }
}
