package com.tcc.api_ticket_sales.application.service.buy_ticket;

import com.tcc.api_ticket_sales.application.mapper.holder.HolderMapper;
import com.tcc.api_ticket_sales.domain.interfaces.PaymentGateway;
import com.tcc.api_ticket_sales.domain.service.HolderDomainService;
import com.tcc.api_ticket_sales.domain.service.OrderDomainService;
import com.tcc.api_ticket_sales.domain.service.TicketDomainService;
import com.tcc.api_ticket_sales.domain.service.TicketTypeDomainService;
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentStatusRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.holder.HolderRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.order.OrderRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket.TicketRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket_type.TicketTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BuyTicketServiceImplTest {

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private HolderMapper holderMapper;

    @Mock
    private PaymentStatusRepository paymentStatusRepository;

    @Mock
    private HolderRepository holderRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketTypeDomainService ticketTypeDomainService;

    @Mock
    private HolderDomainService holderDomainService;

    @Mock
    private OrderDomainService orderDomainService;

    @Mock
    private TicketDomainService ticketDomainService;

    @InjectMocks
    private BuyTicketServiceImpl buyTicketServiceImpl;

}