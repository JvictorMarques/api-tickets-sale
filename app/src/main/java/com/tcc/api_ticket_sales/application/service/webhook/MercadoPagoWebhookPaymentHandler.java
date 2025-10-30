package com.tcc.api_ticket_sales.application.service.webhook;

import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhookPaymentDTO;
import com.tcc.api_ticket_sales.application.exception.OrderNotFoundException;
import com.tcc.api_ticket_sales.application.exception.PaymentMethodNotFoundException;
import com.tcc.api_ticket_sales.application.exception.PaymentStatusNotFoundException;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentMethodEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.domain.enums.PaymentMethodEnum;
import com.tcc.api_ticket_sales.domain.enums.PaymentStatusEnum;
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentMethodRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentStatusRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.order.OrderRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.payment.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MercadoPagoWebhookPaymentHandler {
    private final OrderRepository orderRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentRepository paymentRepository;

    public void handlePaymentEntity(PaymentEntity paymentEntityParam){
        PaymentEntity paymentEntity = paymentRepository.save(paymentEntityParam);

        List<TicketEntity> ticketEntities = paymentEntity.getOrderEntity().getTicketEntities();
        ticketEntities.forEach(ticketEntity -> {
            ticketEntity.setPaymentStatusEntity(paymentEntity.getPaymentStatusEntity());
        });
    }

    public PaymentEntity toPaymentEntity(MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO){
        // ORDEM
        UUID orderId= UUID.fromString(mercadoPagoWebhookPaymentDTO.getOrderId());
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() ->
                new OrderNotFoundException(mercadoPagoWebhookPaymentDTO.getOrderId())
        );

        // STATUS
        PaymentStatusEnum status = PaymentStatusEnum.PENDING;

        switch (mercadoPagoWebhookPaymentDTO.getStatus()){
            case "approved":
                status = PaymentStatusEnum.APPROVED;
                break;
            case "rejected":
                status = PaymentStatusEnum.REJECTED;
                break;
            case "cancelled":
                status = PaymentStatusEnum.CANCELLED;
            case "pending":
                break;
        }

        PaymentStatusEntity paymentStatusEntity= paymentStatusRepository.findByDescription(
                status.getName()
        ).orElseThrow(PaymentStatusNotFoundException::new);

        // METODO
        PaymentMethodEnum method = PaymentMethodEnum.PIX;
        switch (mercadoPagoWebhookPaymentDTO.getMethod()){
            case String m when m.contains("card"):
                method = PaymentMethodEnum.CARD;
                break;
            case "ticket":
                method = PaymentMethodEnum.TICKET;
                break;
            default:
        }

        PaymentMethodEntity paymentMethodEntity= paymentMethodRepository.findByDescription(
                method.getName()
        ).orElseThrow(PaymentMethodNotFoundException::new);

        return PaymentEntity.of(
                mercadoPagoWebhookPaymentDTO.getAmount(),
                orderEntity,
                paymentMethodEntity,
                paymentStatusEntity
        );
    }
}
