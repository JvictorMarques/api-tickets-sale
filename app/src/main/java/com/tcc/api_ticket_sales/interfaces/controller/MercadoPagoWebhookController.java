package com.tcc.api_ticket_sales.interfaces.controller;

import com.tcc.api_ticket_sales.application.service.webhook.MercadoPagoWebhookService;
import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhoookRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks/mercadopago")
@RequiredArgsConstructor
@Tag(name = "Webhooks - MercadoPago")
public class MercadoPagoWebhookController {

    private final MercadoPagoWebhookService mercadoPagoWebhookService;

    @Operation(
            summary = "Receber notificações do MercadoPago"
    )
    @PostMapping
    public ResponseEntity<Void>  receiveNotification(@RequestBody @Valid MercadoPagoWebhoookRequestDTO requestDTO){
        mercadoPagoWebhookService.processNotification(requestDTO);

        return ResponseEntity.ok().build();
    }
}
