package com.tcc.api_ticket_sales.application.service.webhook;

import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhookPaymentDTO;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentMethodEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.domain.enums.PaymentMethodEnum;
import com.tcc.api_ticket_sales.domain.enums.PaymentStatusEnum;
import com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.gateways.MercadoPagoNotificationGateway;
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentMethodRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentStatusRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.order.OrderRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.payment.PaymentRepository;
import com.tcc.api_ticket_sales.application.dto.webhook.mercadopago.MercadoPagoWebhoookRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MercadoPagoWebhookServiceImpl implements MercadoPagoWebhookService {

    private final MercadoPagoNotificationGateway mercadoPagoNotificationGateway;
    private final OrderRepository orderRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void processNotification(MercadoPagoWebhoookRequestDTO requestDTO){
        log.info("MercadoPagoWebhookServiceImpl.processNotification", requestDTO.toString());

        if(requestDTO.getType().equalsIgnoreCase("payment")){
            MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO = mercadoPagoNotificationGateway.processNotificationPayment(requestDTO);
            handlePaymentModel(mercadoPagoWebhookPaymentDTO);
        }

    }

    private void handlePaymentModel(MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO){
        PaymentEntity paymentEntity = paymentRepository.save(toPaymentEntity(mercadoPagoWebhookPaymentDTO));

        List<TicketEntity> ticketEntities = paymentEntity.getOrderEntity().getTicketEntities();
        ticketEntities.forEach(ticketEntity -> {
            ticketEntity.setPaymentStatusEntity(paymentEntity.getPaymentStatusEntity());
        });
    }

    private PaymentEntity toPaymentEntity(MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO){
        // ORDEM
        UUID orderId= UUID.fromString(mercadoPagoWebhookPaymentDTO.getOrderId());
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException("Order id " + orderId + " not found")
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
        ).orElseThrow(() ->
                new EntityNotFoundException("Status de Pagamento não encontrado")
        );

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
        ).orElseThrow(() ->
                new EntityNotFoundException("Método de pagamento não encontrado")
        );

        return PaymentEntity.of(
                mercadoPagoWebhookPaymentDTO.getAmount(),
                orderEntity,
                paymentMethodEntity,
                paymentStatusEntity
        );
    }
}
