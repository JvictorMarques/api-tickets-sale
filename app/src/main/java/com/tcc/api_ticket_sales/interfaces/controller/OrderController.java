package com.tcc.api_ticket_sales.interfaces.controller;

import com.tcc.api_ticket_sales.application.dto.order.OrderResponseDTO;
import com.tcc.api_ticket_sales.application.service.order.OrderService;
import com.tcc.api_ticket_sales.interfaces.controller.exception.RestExceptionMessage;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name = "Order")
public class OrderController {

    private final OrderService orderService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ordem retornada com sucesso"),
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
                                                "message": "Parâmetro 'orderId' inválido: valor [orderId] não pôde ser convertido para o tipo UUID.",
                                                "status": 400,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "capacity": "Parâmetro 'EventId' inválido: valor [orderId] não pôde ser convertido para o tipo UUID.",
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
                                            summary = "Ordem não encontrado",
                                            value = """
                                            {
                                                "message": "Ordem [id] não encontrado.",
                                                "status": 404,
                                                "timeStamp": "2025-10-13T18:00:00",
                                                "errors": [
                                                    "Ordem [id] não encontrado."
                                                ]
                                            }
                                            """
                                    ),
                            }
                    )
            ),
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable UUID orderId){
        OrderResponseDTO response = orderService.getById(orderId);

        return ResponseEntity.ok(response);
    }
}
