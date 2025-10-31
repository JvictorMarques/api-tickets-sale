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
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentMethodRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentStatusRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.order.OrderRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.payment.PaymentRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.tcc.api_ticket_sales.factory.MercadoPagoFactory.createMercadoPagoWebhookPaymentDTO;
import static com.tcc.api_ticket_sales.factory.OrderFactory.createOrderEntityWithId;
import static com.tcc.api_ticket_sales.factory.PaymentFactory.createPaymentEntityWithId;
import static com.tcc.api_ticket_sales.factory.PaymentMethodFactory.createPaymentMethodEntity;
import static com.tcc.api_ticket_sales.factory.PaymentStatusFactory.createPaymentStatusEntity;
import static com.tcc.api_ticket_sales.factory.TicketFactory.createListTicketEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MercadoPagoWebhookPaymentHandlerTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentStatusRepository paymentStatusRepository;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private MercadoPagoWebhookPaymentHandler paymentHandler;

    @Tag("unit")
    @Test
    void handlePaymentEntity_ShouldReturnUpdateTicketsStatus_WhenSuccess() {
        // Arrange
        List<TicketEntity> tickets = createListTicketEntity();
        PaymentEntity paymentEntity = createPaymentEntityWithId();
        PaymentStatusEntity paymentStatusEntity = paymentEntity.getPaymentStatusEntity();
        paymentEntity.getOrderEntity().setTicketEntities(tickets);

        when(paymentRepository.save(any())).thenReturn(paymentEntity);

        // Act
        paymentHandler.handlePaymentEntity(paymentEntity);

        // Assert
        verify(paymentRepository, times(1)).save(paymentEntity);
        assertEquals(paymentStatusEntity, tickets.getFirst().getPaymentStatusEntity());
    }

    @Tag("unit")
    @Test
    void toPaymentEntity_ShouldThrowOrderNotFoundException_WhenOrderNotFound() {
        MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO = createMercadoPagoWebhookPaymentDTO();

        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            paymentHandler.toPaymentEntity(mercadoPagoWebhookPaymentDTO);
        });
    }

    @Tag("unit")
    @Test
    void toPaymentEntity_ShouldThrowPaymentStatusNotFoundException_WhenPaymentStatusNotFound() {
        MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO = createMercadoPagoWebhookPaymentDTO();
        OrderEntity orderEntity = createOrderEntityWithId();

        when(orderRepository.findById(any())).thenReturn(Optional.of(orderEntity));
        when(paymentStatusRepository.findByDescription(any())).thenReturn(Optional.empty());

        assertThrows(PaymentStatusNotFoundException.class, () -> {
            paymentHandler.toPaymentEntity(mercadoPagoWebhookPaymentDTO);
        });
    }

    @Tag("unit")
    @Test
    void toPaymentEntity_ShouldThrowPaymentMethodNotFoundException_WhenPaymentMethodNotFound() {
        MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO = createMercadoPagoWebhookPaymentDTO();
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();
        OrderEntity orderEntity = createOrderEntityWithId();

        when(orderRepository.findById(any())).thenReturn(Optional.of(orderEntity));
        when(paymentStatusRepository.findByDescription(any())).thenReturn(Optional.of(paymentStatusEntity));
        when(paymentMethodRepository.findByDescription(any())).thenReturn(Optional.empty());

        assertThrows(PaymentMethodNotFoundException.class, () -> {
            paymentHandler.toPaymentEntity(mercadoPagoWebhookPaymentDTO);
        });
    }

    @Tag("unit")
    @Test
    void toPaymentEntity_ShouldReturnPaymentEntityWithStatusApproved_WhenMercadoPagoWebhookPaymentStatusApproved() {
        MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO = createMercadoPagoWebhookPaymentDTO();
        mercadoPagoWebhookPaymentDTO.setStatus("approved");
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();
        PaymentMethodEntity paymentMethodEntity = createPaymentMethodEntity();
        OrderEntity orderEntity = createOrderEntityWithId();

        when(orderRepository.findById(any())).thenReturn(Optional.of(orderEntity));
        when(paymentStatusRepository.findByDescription(anyString())).thenReturn(Optional.of(paymentStatusEntity));
        when(paymentMethodRepository.findByDescription(any())).thenReturn(Optional.of(paymentMethodEntity));

        PaymentEntity paymentEntity = paymentHandler.toPaymentEntity(mercadoPagoWebhookPaymentDTO);

        verify(paymentStatusRepository, times(1)).findByDescription("approved");
        assertEquals(paymentEntity.getPaymentStatusEntity(), paymentStatusEntity);
        assertEquals(paymentEntity.getPaymentMethodEntity(), paymentMethodEntity);
        assertEquals(paymentEntity.getOrderEntity(), orderEntity);
        assertEquals(paymentEntity.getAmount(), mercadoPagoWebhookPaymentDTO.getAmount());
    }

    @Tag("unit")
    @Test
    void toPaymentEntity_ShouldReturnPaymentEntityWithStatusRejected_WhenMercadoPagoWebhookPaymentStatusRejected() {
        MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO = createMercadoPagoWebhookPaymentDTO();
        mercadoPagoWebhookPaymentDTO.setStatus("rejected");
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();
        PaymentMethodEntity paymentMethodEntity = createPaymentMethodEntity();
        OrderEntity orderEntity = createOrderEntityWithId();

        when(orderRepository.findById(any())).thenReturn(Optional.of(orderEntity));
        when(paymentStatusRepository.findByDescription(anyString())).thenReturn(Optional.of(paymentStatusEntity));
        when(paymentMethodRepository.findByDescription(any())).thenReturn(Optional.of(paymentMethodEntity));

        PaymentEntity paymentEntity = paymentHandler.toPaymentEntity(mercadoPagoWebhookPaymentDTO);

        verify(paymentStatusRepository, times(1)).findByDescription("rejected");
        assertEquals(paymentEntity.getPaymentStatusEntity(), paymentStatusEntity);
        assertEquals(paymentEntity.getPaymentMethodEntity(), paymentMethodEntity);
        assertEquals(paymentEntity.getOrderEntity(), orderEntity);
        assertEquals(paymentEntity.getAmount(), mercadoPagoWebhookPaymentDTO.getAmount());
    }

    @Tag("unit")
    @Test
    void toPaymentEntity_ShouldReturnPaymentEntityWithStatusCancelled_WhenMercadoPagoWebhookPaymentStatusCancelled() {
        MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO = createMercadoPagoWebhookPaymentDTO();
        mercadoPagoWebhookPaymentDTO.setStatus("cancelled");
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();
        PaymentMethodEntity paymentMethodEntity = createPaymentMethodEntity();
        OrderEntity orderEntity = createOrderEntityWithId();

        when(orderRepository.findById(any())).thenReturn(Optional.of(orderEntity));
        when(paymentStatusRepository.findByDescription(anyString())).thenReturn(Optional.of(paymentStatusEntity));
        when(paymentMethodRepository.findByDescription(any())).thenReturn(Optional.of(paymentMethodEntity));

        PaymentEntity paymentEntity = paymentHandler.toPaymentEntity(mercadoPagoWebhookPaymentDTO);

        verify(paymentStatusRepository, times(1)).findByDescription("cancelled");
        assertEquals(paymentEntity.getPaymentStatusEntity(), paymentStatusEntity);
        assertEquals(paymentEntity.getPaymentMethodEntity(), paymentMethodEntity);
        assertEquals(paymentEntity.getOrderEntity(), orderEntity);
        assertEquals(paymentEntity.getAmount(), mercadoPagoWebhookPaymentDTO.getAmount());
    }

    @Tag("unit")
    @Test
    void toPaymentEntity_ShouldReturnPaymentEntityWithStatusPending_WhenMercadoPagoWebhookPaymentStatusPending() {
        MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO = createMercadoPagoWebhookPaymentDTO();
        mercadoPagoWebhookPaymentDTO.setStatus("pending");
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();
        PaymentMethodEntity paymentMethodEntity = createPaymentMethodEntity();
        OrderEntity orderEntity = createOrderEntityWithId();

        when(orderRepository.findById(any())).thenReturn(Optional.of(orderEntity));
        when(paymentStatusRepository.findByDescription(anyString())).thenReturn(Optional.of(paymentStatusEntity));
        when(paymentMethodRepository.findByDescription(any())).thenReturn(Optional.of(paymentMethodEntity));

        PaymentEntity paymentEntity = paymentHandler.toPaymentEntity(mercadoPagoWebhookPaymentDTO);

        verify(paymentStatusRepository, times(1)).findByDescription("pending");
        assertEquals(paymentEntity.getPaymentStatusEntity(), paymentStatusEntity);
        assertEquals(paymentEntity.getPaymentMethodEntity(), paymentMethodEntity);
        assertEquals(paymentEntity.getOrderEntity(), orderEntity);
        assertEquals(paymentEntity.getAmount(), mercadoPagoWebhookPaymentDTO.getAmount());
    }

    @Tag("unit")
    @Test
    void toPaymentEntity_ShouldReturnPaymentEntityWithStatusPending_WhenMercadoPagoWebhookPaymentStatusNoMapped() {
        MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO = createMercadoPagoWebhookPaymentDTO();
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();
        PaymentMethodEntity paymentMethodEntity = createPaymentMethodEntity();
        OrderEntity orderEntity = createOrderEntityWithId();

        when(orderRepository.findById(any())).thenReturn(Optional.of(orderEntity));
        when(paymentStatusRepository.findByDescription(anyString())).thenReturn(Optional.of(paymentStatusEntity));
        when(paymentMethodRepository.findByDescription(any())).thenReturn(Optional.of(paymentMethodEntity));

        PaymentEntity paymentEntity = paymentHandler.toPaymentEntity(mercadoPagoWebhookPaymentDTO);

        verify(paymentStatusRepository, times(1)).findByDescription("pending");
        assertEquals(paymentEntity.getPaymentStatusEntity(), paymentStatusEntity);
        assertEquals(paymentEntity.getPaymentMethodEntity(), paymentMethodEntity);
        assertEquals(paymentEntity.getOrderEntity(), orderEntity);
        assertEquals(paymentEntity.getAmount(), mercadoPagoWebhookPaymentDTO.getAmount());
    }

    @Tag("unit")
    @Test
    void toPaymentEntity_ShouldReturnPaymentEntityWithMethodPix_WhenMercadoPagoWebhookPaymentMethodNoMapped() {
        MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO = createMercadoPagoWebhookPaymentDTO();
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();
        PaymentMethodEntity paymentMethodEntity = createPaymentMethodEntity();
        OrderEntity orderEntity = createOrderEntityWithId();

        when(orderRepository.findById(any())).thenReturn(Optional.of(orderEntity));
        when(paymentStatusRepository.findByDescription(anyString())).thenReturn(Optional.of(paymentStatusEntity));
        when(paymentMethodRepository.findByDescription(anyString())).thenReturn(Optional.of(paymentMethodEntity));

        PaymentEntity paymentEntity = paymentHandler.toPaymentEntity(mercadoPagoWebhookPaymentDTO);

        verify(paymentMethodRepository, times(1)).findByDescription("pix");
        assertEquals(paymentEntity.getPaymentStatusEntity(), paymentStatusEntity);
        assertEquals(paymentEntity.getPaymentMethodEntity(), paymentMethodEntity);
        assertEquals(paymentEntity.getOrderEntity(), orderEntity);
        assertEquals(paymentEntity.getAmount(), mercadoPagoWebhookPaymentDTO.getAmount());
    }

    @Tag("unit")
    @Test
    void toPaymentEntity_ShouldReturnPaymentEntityWithMethodCard_WhenMercadoPagoWebhookPaymentMethodCard() {
        MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO = createMercadoPagoWebhookPaymentDTO();
        mercadoPagoWebhookPaymentDTO.setMethod("card");
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();
        PaymentMethodEntity paymentMethodEntity = createPaymentMethodEntity();
        OrderEntity orderEntity = createOrderEntityWithId();

        when(orderRepository.findById(any())).thenReturn(Optional.of(orderEntity));
        when(paymentStatusRepository.findByDescription(anyString())).thenReturn(Optional.of(paymentStatusEntity));
        when(paymentMethodRepository.findByDescription(anyString())).thenReturn(Optional.of(paymentMethodEntity));

        PaymentEntity paymentEntity = paymentHandler.toPaymentEntity(mercadoPagoWebhookPaymentDTO);

        verify(paymentMethodRepository, times(1)).findByDescription("card");
        assertEquals(paymentEntity.getPaymentStatusEntity(), paymentStatusEntity);
        assertEquals(paymentEntity.getPaymentMethodEntity(), paymentMethodEntity);
        assertEquals(paymentEntity.getOrderEntity(), orderEntity);
        assertEquals(paymentEntity.getAmount(), mercadoPagoWebhookPaymentDTO.getAmount());
    }

    @Tag("unit")
    @Test
    void toPaymentEntity_ShouldReturnPaymentEntityWithMethodTicket_WhenMercadoPagoWebhookPaymentMethodTicket() {
        MercadoPagoWebhookPaymentDTO mercadoPagoWebhookPaymentDTO = createMercadoPagoWebhookPaymentDTO();
        mercadoPagoWebhookPaymentDTO.setMethod("ticket");
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();
        PaymentMethodEntity paymentMethodEntity = createPaymentMethodEntity();
        OrderEntity orderEntity = createOrderEntityWithId();

        when(orderRepository.findById(any())).thenReturn(Optional.of(orderEntity));
        when(paymentStatusRepository.findByDescription(anyString())).thenReturn(Optional.of(paymentStatusEntity));
        when(paymentMethodRepository.findByDescription(anyString())).thenReturn(Optional.of(paymentMethodEntity));

        PaymentEntity paymentEntity = paymentHandler.toPaymentEntity(mercadoPagoWebhookPaymentDTO);

        verify(paymentMethodRepository, times(1)).findByDescription("ticket");
        assertEquals(paymentEntity.getPaymentStatusEntity(), paymentStatusEntity);
        assertEquals(paymentEntity.getPaymentMethodEntity(), paymentMethodEntity);
        assertEquals(paymentEntity.getOrderEntity(), orderEntity);
        assertEquals(paymentEntity.getAmount(), mercadoPagoWebhookPaymentDTO.getAmount());
    }
}