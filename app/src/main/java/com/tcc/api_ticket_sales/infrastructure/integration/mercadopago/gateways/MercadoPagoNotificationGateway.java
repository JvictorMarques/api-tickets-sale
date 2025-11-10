package com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.gateways;

import com.mercadopago.resources.payment.Payment;
import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhookPaymentDTO;
import com.tcc.api_ticket_sales.domain.exception.BadGatewayException;
import com.tcc.api_ticket_sales.domain.exception.BadRequestException;
import com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.MercadoPagoClient;
import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhoookRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MercadoPagoNotificationGateway {

    private final MercadoPagoClient mercadoPagoClient;

    public MercadoPagoWebhookPaymentDTO processNotificationPayment (MercadoPagoWebhoookRequestDTO requestDTO){
        try{
            Long paymentId = Long.parseLong(requestDTO.getData().getId());
            Payment payment = mercadoPagoClient.getPayment(paymentId);

            if(payment == null){
                throw new BadRequestException("Pagamento do mercado pago não encontrado");
            }

            String paymentStatus = payment.getStatus();
            String paymentMethod = payment.getPaymentTypeId();
            String orderId = payment.getExternalReference();


            return MercadoPagoWebhookPaymentDTO.builder()
                    .amount(payment.getTransactionAmount())
                    .status(paymentStatus)
                    .method(paymentMethod)
                    .orderId(orderId)
                    .build();

        }catch (Exception e){
            throw new BadGatewayException(String.format("Integração Mercado Pago: %s", e.getMessage()));
        }
    }
}
