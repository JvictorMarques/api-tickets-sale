package com.tcc.api_ticket_sales.application.service.buy_ticket;

import com.tcc.api_ticket_sales.application.exception.PaymentStatusNotFoundException;
import com.tcc.api_ticket_sales.application.exception.TicketTypeNotFoundException;
import com.tcc.api_ticket_sales.application.mapper.payer.PayerMapper;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.enums.PaymentStatusEnum;
import com.tcc.api_ticket_sales.domain.interfaces.PaymentGateway;
import com.tcc.api_ticket_sales.domain.model.TicketBuyModel;
import com.tcc.api_ticket_sales.domain.service.OrderDomainService;
import com.tcc.api_ticket_sales.domain.service.TicketTypeDomainService;
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentStatusRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.order.OrderRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket_type.TicketTypeRepository;
import com.tcc.api_ticket_sales.application.dto.buy_ticket.BuyTicketRequestDTO;
import com.tcc.api_ticket_sales.application.dto.buy_ticket.BuyTicketResponseDTO;
import com.tcc.api_ticket_sales.application.dto.payment.ItemPaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BuyTicketServiceImpl implements BuyTicketService{

    private final TicketTypeRepository ticketTypeRepository;

    private final PaymentGateway paymentGateway;

    private final PaymentStatusRepository paymentStatusRepository;

    private final OrderRepository orderRepository;

    private final TicketTypeDomainService ticketTypeDomainService;

    private final OrderDomainService orderDomainService;

    private final BuyTicketHandler buyTicketHandler;

    private final BuyTicketValidator buyTicketValidator;

    private final PayerMapper payerMapper;


    @Transactional
    public BuyTicketResponseDTO buyTickets(BuyTicketRequestDTO buyTicketRequestDTO){
        buyTicketValidator.validate(buyTicketRequestDTO);

        PaymentStatusEntity paymentStatusPending = paymentStatusRepository.findByDescription(
                PaymentStatusEnum.PENDING.getName()
        ).orElseThrow(
                PaymentStatusNotFoundException::new
        );

        List<TicketBuyModel> ticketBuyModels = buyTicketRequestDTO.getTickets().stream().map(
                ticketRequestDTO -> {
                    TicketTypeEntity ticketTypeEntity = ticketTypeRepository.findById(ticketRequestDTO.getId()).orElseThrow(
                            () -> new TicketTypeNotFoundException(ticketRequestDTO.getId().toString())
                    );
                    return TicketBuyModel.builder()
                            .ticketTypeEntity(ticketTypeEntity)
                            .quantity(ticketRequestDTO.getHolders().size())
                            .build();
                }
        ).toList();

        BigDecimal totalPrice = ticketTypeDomainService.calculateTotalPrice(ticketBuyModels);
        OrderEntity orderEntity = orderDomainService.createOrder(totalPrice);
        orderRepository.save(orderEntity);

        List<ItemPaymentRequestDTO> itemPaymentRequestDTOS = buyTicketHandler.processTickets(
                buyTicketRequestDTO.getTickets(),
                orderEntity,
                paymentStatusPending
        );

        PaymentRequestDTO paymentRequestDTO = PaymentRequestDTO.builder()
                .itemPaymentRequestDTOList(itemPaymentRequestDTOS)
                .payerPaymentRequestDTO(payerMapper.fromPayerRequestDTOToPayerPaymentRequestDTO(
                        buyTicketRequestDTO.getPayer()
                ))
                .orderId(orderEntity.getId().toString())
                .build();

        PaymentResponseDTO response = paymentGateway.createPreference(paymentRequestDTO);


        return BuyTicketResponseDTO.builder()
                .orderId(response.getOrderId())
                .redirectUrl(response.getRedirectUrl())
                .build();
    }
}
