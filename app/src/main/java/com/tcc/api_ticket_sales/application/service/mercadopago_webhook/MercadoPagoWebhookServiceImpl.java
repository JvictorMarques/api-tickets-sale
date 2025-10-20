package com.tcc.api_ticket_sales.application.service.mercadopago_webhook;

import com.tcc.api_ticket_sales.application.model.PaymentModel;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentMethodEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.domain.enums.PaymentMethodEnum;
import com.tcc.api_ticket_sales.domain.enums.PaymentStatusEnum;
import com.tcc.api_ticket_sales.infrastructure.integration.mercadopago.notification.MercadoPagoNotificationGateway;
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentMethodRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentStatusRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.order.OrderRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.payment.PaymentRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket.TicketRepository;
import com.tcc.api_ticket_sales.interfaces.dto.webhook.mercadopago.MercadoPagoWebhoookRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MercadoPagoWebhookServiceImpl implements MercadoPagoWebhookService {

    private final MercadoPagoNotificationGateway mercadoPagoNotificationGateway;
    private final OrderRepository orderRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public void processNotification(MercadoPagoWebhoookRequestDTO requestDTO){
        if(requestDTO.getType().equalsIgnoreCase("payment")){
            PaymentModel paymentModel = mercadoPagoNotificationGateway.processNotificationPayment(requestDTO);
            handlePaymentModel(paymentModel);
        }

    }

    private void handlePaymentModel(PaymentModel paymentModel){
        PaymentEntity paymentEntity = paymentRepository.save(toPaymentEntity(paymentModel));

        List<TicketEntity> ticketEntities = paymentEntity.getOrderEntity().getTicketEntities();
        ticketEntities.forEach(ticketEntity -> {
            ticketEntity.setPaymentStatusEntity(paymentEntity.getPaymentStatusEntity());
        });
    }

    private PaymentEntity toPaymentEntity(PaymentModel paymentModel){
        // ORDEM
        UUID orderId= UUID.fromString(paymentModel.getOrderId());
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException("Order id " + orderId + " not found")
        );

        // STATUS
        PaymentStatusEnum status = PaymentStatusEnum.PENDING;

        switch (paymentModel.getStatus()){
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
                new EntityNotFoundException("Status Payment not found")
        );

        // METODO
        PaymentMethodEnum method = PaymentMethodEnum.PIX;
        switch (paymentModel.getMethod()){
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
                new EntityNotFoundException("Method Payment not found")
        );

        return PaymentEntity.of(
                paymentModel.getAmount(),
                orderEntity,
                paymentMethodEntity,
                paymentStatusEntity
        );
    }
}
