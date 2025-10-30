package com.tcc.api_ticket_sales.application.service.buy_ticket;

import com.tcc.api_ticket_sales.application.dto.payment.ItemPaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.ticket.TicketCreateRequestDTO;
import com.tcc.api_ticket_sales.application.exception.TicketTypeNotFoundException;
import com.tcc.api_ticket_sales.application.mapper.holder.HolderMapper;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.service.HolderDomainService;
import com.tcc.api_ticket_sales.domain.service.TicketDomainService;
import com.tcc.api_ticket_sales.infrastructure.repository.holder.HolderRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket.TicketRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket_type.TicketTypeRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.tcc.api_ticket_sales.factory.BuyTicketFactory.createListTicketCreateRequestDTO;
import static com.tcc.api_ticket_sales.factory.OrderFactory.createOrderEntityWithId;
import static com.tcc.api_ticket_sales.factory.PaymentStatusFactory.createPaymentStatusEntity;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeEntityWithId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BuyTicketHandlerTest {

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    @Mock
    private TicketDomainService ticketDomainService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private HolderRepository holderRepository;

    @Mock
    private HolderDomainService holderDomainService;

    @Mock
    private HolderMapper holderMapper;

    @InjectMocks
    private BuyTicketHandler buyTicketHandler;

    @Tag("unit")
    @Test
    void processTickets_ShouldReturnTicketNotFoundException_WhenTicketTypeDoesNotExist() {
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();
        OrderEntity orderEntity = createOrderEntityWithId();

        assertThrows(TicketTypeNotFoundException.class, () -> {
            buyTicketHandler.processTickets(createListTicketCreateRequestDTO(), orderEntity, paymentStatusEntity);
        });
    }

    @Tag("unit")
    @Test
    void processTickets_ShouldReturnListEmpty_WhenTicketsEmpty() {
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();
        OrderEntity orderEntity = createOrderEntityWithId();


        List<ItemPaymentRequestDTO> result = buyTicketHandler.processTickets(List.of(), orderEntity, paymentStatusEntity);
        assertTrue(result.isEmpty());
    }

    @Tag("unit")
    @Test
    void processTickets_ShouldReturnListItems_WhenSuccess() {
        PaymentStatusEntity paymentStatusEntity = createPaymentStatusEntity();
        OrderEntity orderEntity = createOrderEntityWithId();
        List<TicketCreateRequestDTO> tickets = createListTicketCreateRequestDTO();
        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithId();

        when(ticketTypeRepository.findById(any())).thenReturn(Optional.of(ticketTypeEntity));
        when(holderMapper.fromHolderCreateRequestDTOToHolderEntity(any())).thenReturn(null);
        when(holderRepository.findByNameAndEmail(any(), any())).thenReturn(null);
        when(holderRepository.save(any())).thenReturn(null);
        when(holderDomainService.creatOrReturnExistsHolderEntity(any(), any())).thenReturn(null);
        when(ticketDomainService.createTicket(any(), any(), any(), any())).thenReturn(null);
        when(ticketRepository.save(any())).thenReturn(null);

        List<ItemPaymentRequestDTO> result = buyTicketHandler.processTickets(tickets, orderEntity, paymentStatusEntity);
        assertEquals(result.size(), tickets.size());
    }
}