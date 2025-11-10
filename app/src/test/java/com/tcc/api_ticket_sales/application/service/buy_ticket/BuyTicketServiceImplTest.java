package com.tcc.api_ticket_sales.application.service.buy_ticket;

import com.tcc.api_ticket_sales.application.dto.buy_ticket.BuyTicketRequestDTO;
import com.tcc.api_ticket_sales.application.dto.buy_ticket.BuyTicketResponseDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentResponseDTO;
import com.tcc.api_ticket_sales.application.exception.PaymentStatusNotFoundException;
import com.tcc.api_ticket_sales.application.exception.TicketTypeNotFoundException;
import com.tcc.api_ticket_sales.application.mapper.payer.PayerMapper;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.interfaces.PaymentGateway;
import com.tcc.api_ticket_sales.domain.service.OrderDomainService;
import com.tcc.api_ticket_sales.domain.service.TicketTypeDomainService;
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentStatusRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.order.OrderRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket_type.TicketTypeRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.tcc.api_ticket_sales.factory.BuyTicketFactory.createBuyTicketRequestDTO;
import static com.tcc.api_ticket_sales.factory.BuyTicketFactory.createPaymentResponseDTO;
import static com.tcc.api_ticket_sales.factory.OrderFactory.createOrderEntityWithId;
import static com.tcc.api_ticket_sales.factory.PaymentStatusFactory.createPaymentStatusEntity;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeEntityWithId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BuyTicketServiceImplTest {

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private PaymentStatusRepository paymentStatusRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TicketTypeDomainService ticketTypeDomainService;

    @Mock
    private OrderDomainService orderDomainService;

    @Mock
    private BuyTicketHandler buyTicketHandler;

    @Mock
    private BuyTicketValidator buyTicketValidator;

    @Mock
    private PayerMapper payerMapper;

    @InjectMocks
    private BuyTicketServiceImpl buyTicketServiceImpl;

    @Tag("unit")
    @Test
    void buyTickets_ShouldPaymentStatusNotFoundException_WhenPaymentStatusNotFound() {
        BuyTicketRequestDTO buyTicketRequestDTO = new BuyTicketRequestDTO();
        doNothing().when(buyTicketValidator).validate(any());

        when(paymentStatusRepository.findByDescription(any())).thenReturn(Optional.empty());

        assertThrows(PaymentStatusNotFoundException.class, () -> buyTicketServiceImpl.buyTickets(buyTicketRequestDTO));
    }


    @Tag("unit")
    @Test
    void buyTickets_ShouldTicketTypeNotFoundException_WhenTicketTypeNotFound() {
        BuyTicketRequestDTO buyTicketRequestDTO = createBuyTicketRequestDTO();
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();

        doNothing().when(buyTicketValidator).validate(any());

        when(paymentStatusRepository.findByDescription(any())).thenReturn(Optional.of(paymentStatusEntity));


        assertThrows(TicketTypeNotFoundException.class, () -> buyTicketServiceImpl.buyTickets(buyTicketRequestDTO));
    }

    @Tag("unit")
    @Test
    void buyTickets_ShouldBuyTicketResponseDTO_WhenSuccess() {
        BuyTicketRequestDTO buyTicketRequestDTO = createBuyTicketRequestDTO();
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();
        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithId();
        OrderEntity orderEntity = createOrderEntityWithId();
        PaymentResponseDTO paymentResponseDTO= createPaymentResponseDTO();

        doNothing().when(buyTicketValidator).validate(any());

        when(paymentStatusRepository.findByDescription(any())).thenReturn(Optional.of(paymentStatusEntity));
        when(ticketTypeRepository.findById(any())).thenReturn(Optional.of(ticketTypeEntity));

        when(ticketTypeDomainService.calculateTotalPrice(any())).thenReturn(ticketTypeEntity.getPrice());

        when(orderDomainService.createOrder(any())).thenReturn(orderEntity);
        when(orderRepository.save(any())).thenReturn(null);

        when(buyTicketHandler.processTickets(any(), any(), any())).thenReturn(List.of());

        when(paymentGateway.createPayment(any())).thenReturn(paymentResponseDTO);

        when(payerMapper.fromPayerRequestDTOToPayerPaymentRequestDTO(any())).thenReturn(null);


        BuyTicketResponseDTO buyTicketResponseDTO= buyTicketServiceImpl.buyTickets(buyTicketRequestDTO);

        assertEquals(buyTicketResponseDTO.getOrderId(), orderEntity.getId());
        assertEquals(buyTicketResponseDTO.getStatus(), paymentResponseDTO.getStatus());
    }
}