package com.tcc.api_ticket_sales.application.service.buy_ticket;

import com.tcc.api_ticket_sales.domain.entity.HolderEntity;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.enums.PaymentStatusEnum;
import com.tcc.api_ticket_sales.domain.exception.BusinessException;
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
import com.tcc.api_ticket_sales.application.dto.buy_ticket.BuyTicketRequestDTO;
import com.tcc.api_ticket_sales.application.dto.buy_ticket.BuyTicketResponseDTO;
import com.tcc.api_ticket_sales.application.dto.holder.HolderCreateRequestDTO;
import com.tcc.api_ticket_sales.application.mapper.holder.HolderMapper;
import com.tcc.api_ticket_sales.application.dto.payment.ItemPaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PayerPaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.PaymentResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BuyTicketServiceImpl implements BuyTicketService{

    private final TicketTypeRepository ticketTypeRepository;

    private final PaymentGateway paymentGateway;

    private final HolderMapper holderMapper;

    private final PaymentStatusRepository paymentStatusRepository;

    private final HolderRepository holderRepository;

    private final OrderRepository orderRepository;

    private final TicketRepository ticketRepository;

    private final TicketTypeDomainService ticketTypeDomainService;

    private final HolderDomainService holderDomainService;

    private final OrderDomainService orderDomainService;

    private final TicketDomainService ticketDomainService;


    @Transactional
    public BuyTicketResponseDTO buyTickets(BuyTicketRequestDTO buyTicketRequestDTO){
        Map<UUID, Integer> quantityByTicket = new HashMap<>();

        PaymentStatusEntity paymentStatusPending = paymentStatusRepository.findByDescription(
                PaymentStatusEnum.PENDING.getName()
        ).orElseThrow(
                () -> new EntityNotFoundException("Status do pagamento não encontrado")
        );

        List<ItemPaymentRequestDTO> itemPaymentRequestDTOS = new ArrayList<>();


        BigDecimal totalPrice = getTotalPriceOrder(buyTicketRequestDTO);
        OrderEntity orderEntity = orderDomainService.createOrder(totalPrice);
        orderRepository.save(orderEntity);

        // validações
        buyTicketRequestDTO.getTickets().forEach(ticketRequestDTO -> {
            checkDuplicateHolder(ticketRequestDTO.getHolders());

            TicketTypeEntity ticketType = ticketTypeRepository.findById(ticketRequestDTO.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Ingresso não encontrado para compra")
            );

            ticketTypeDomainService.validateTicketTypeSale(ticketType);

            int quantityHolders = ticketRequestDTO.getHolders().size();

            if(!ticketType.getTicketEntities().isEmpty()) {
                if(quantityByTicket.containsKey(ticketRequestDTO.getId())){
                    quantityByTicket.put(
                            ticketRequestDTO.getId(),
                            quantityByTicket.get(ticketRequestDTO.getId()) + quantityHolders
                    );
                }else{
                    quantityByTicket.put(ticketRequestDTO.getId(), quantityHolders);
                }

                ticketTypeDomainService.validateCapacity(ticketType, quantityByTicket.get(ticketRequestDTO.getId()));
            }

            ticketRequestDTO.getHolders().forEach((holderDTO) -> {
                List<HolderEntity> holderEntitiesFind = holderRepository.findByNameAndEmail(holderDTO.getName(), holderDTO.getEmail());

                HolderEntity holderEntity = holderDomainService.creatOrReturnExistsHolderEntity(
                        holderMapper.fromHolderCreateRequestDTOToHolderEntity(holderDTO),
                        holderEntitiesFind
                );

                holderRepository.save(holderEntity);

                TicketEntity ticketEntity = ticketDomainService.createTicket(
                        ticketType,
                        holderEntity,
                        orderEntity,
                        paymentStatusPending
                );

                ticketRepository.save(ticketEntity);
            });

            // adicionar no mapper
            itemPaymentRequestDTOS.add(ItemPaymentRequestDTO.builder()
                            .id(ticketType.getId().toString())
                            .title(ticketType.getName())
                            .description(ticketType.getDescription())
                            .unitPrice(ticketType.getPrice())
                            .quantity(quantityHolders)
                    .build());
        });

        PayerPaymentRequestDTO payerPaymentRequestDTO = PayerPaymentRequestDTO.builder()
                .name(buyTicketRequestDTO.getPayer().getName())
                .email(buyTicketRequestDTO.getPayer().getEmail())
                .build();

        PaymentRequestDTO paymentRequestDTO = PaymentRequestDTO.builder()
                .itemPaymentRequestDTOList(itemPaymentRequestDTOS)
                .payerPaymentRequestDTO(payerPaymentRequestDTO)
                .orderId(orderEntity.getId().toString())
                .build();

        PaymentResponseDTO response = paymentGateway.createPreference(paymentRequestDTO);


        return BuyTicketResponseDTO.builder()
                .orderId(response.getOrderId())
                .redirectUrl(response.getRedirectUrl())
                .build();
    }


    private void checkDuplicateHolder(List<HolderCreateRequestDTO> holders) {
        Map<String, String> holderExists = new HashMap<>();
        holders.forEach(holder -> {
            String holderName = holder.getName();
            String holderEmail = holder.getEmail();

            if(holderExists.containsKey(holderName) && holderExists.get(holderName).equals(holderEmail)){
                throw new BusinessException("Não é possível comprar mais de um ingresso para o mesmo titular.");
            }else{
                holderExists.put(holderName, holderEmail);
            }
        });
    }


    private BigDecimal getTotalPriceOrder(BuyTicketRequestDTO buyTicketRequestDTO){
        BigDecimal totalPrice = BigDecimal.ZERO;
        buyTicketRequestDTO.getTickets().forEach(ticketRequestDTO -> {
            TicketTypeEntity ticketType = ticketTypeRepository.findById(ticketRequestDTO.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Ingresso não encontrado para compra")
            );

            ticketType.getPrice().multiply(new BigDecimal(ticketRequestDTO.getHolders().size())).add(
                    totalPrice
            );
        });

        return totalPrice;
    }

}
