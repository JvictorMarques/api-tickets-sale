package com.tcc.api_ticket_sales.application.service.buy_ticket;

import com.tcc.api_ticket_sales.application.exception.EventClosedException;
import com.tcc.api_ticket_sales.application.model.BuyTicketRequest;
import com.tcc.api_ticket_sales.application.model.BuyTicketResponse;
import com.tcc.api_ticket_sales.application.model.PayerRequest;
import com.tcc.api_ticket_sales.application.model.TicketRequest;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.HolderEntity;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.enums.PaymentStatusEnum;
import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import com.tcc.api_ticket_sales.domain.interfaces.PaymentGateway;
import com.tcc.api_ticket_sales.infrastructure.repository.PaymentStatusRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.holder.HolderRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.holder.HolderSpecification;
import com.tcc.api_ticket_sales.infrastructure.repository.order.OrderRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket.TicketRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket_type.TicketTypeRepository;
import com.tcc.api_ticket_sales.interfaces.dto.buy_ticket.BuyTicketRequestDTO;
import com.tcc.api_ticket_sales.interfaces.dto.buy_ticket.BuyTicketResponseDTO;
import com.tcc.api_ticket_sales.interfaces.dto.holder.HolderCreateRequestDTO;
import com.tcc.api_ticket_sales.interfaces.mapper.holder.HolderMapper;
import com.tcc.api_ticket_sales.interfaces.mapper.payer.PayerMapper;
import com.tcc.api_ticket_sales.interfaces.mapper.ticket_type.TicketTypeMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BuyTicketServiceImpl implements BuyTicketService{

    private final TicketTypeRepository ticketTypeRepository;

    private final TicketTypeMapper ticketTypeMapper;

    private final PaymentGateway paymentGateway;

    private final PaymentStatusRepository paymentStatusRepository;

    private final PayerMapper payerMapper;

    private final HolderMapper holderMapper;

    private final HolderRepository holderRepository;

    private final OrderRepository orderRepository;

    private final TicketRepository ticketRepository;



    @Transactional
    public BuyTicketResponseDTO buyTickets(BuyTicketRequestDTO buyTicketRequestDTO){
        List<TicketRequest> ticketRequests = new ArrayList<>();

        Map<UUID, Integer> quantityByTicket = new HashMap<>();

        // validações
        buyTicketRequestDTO.getTickets().forEach(ticketRequestDTO -> {
            TicketTypeEntity ticketType = ticketTypeRepository.findById(ticketRequestDTO.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Ingresso não encontrado para compra")
            );

            if(ticketType.getDeletedAt() != null){
                throw new EntityNotFoundException("Ingresso não encontrado para compra");
            }

            EventEntity eventEntity = ticketType.getEventEntity();
            int quantityHolders = ticketRequestDTO.getHolders().size();

            if(eventEntity.isClosed()){
                throw new EventClosedException();
            }

            if(ticketType.getDateFinal().isBefore(LocalDateTime.now())){
                throw new BusinessException("Data de venda do ingresso já foi encerrada.");
            }

            if(!ticketType.getTicketEntities().isEmpty()) {
                if(quantityByTicket.containsKey(ticketRequestDTO.getId())){
                    quantityByTicket.put(
                            ticketRequestDTO.getId(),
                            quantityByTicket.get(ticketRequestDTO.getId()) + quantityHolders
                    );
                }else{
                    quantityByTicket.put(ticketRequestDTO.getId(), quantityHolders);
                }

                checkCapacityEvent(ticketType, quantityByTicket.get(ticketRequestDTO.getId()));
                checkHolderExists(ticketRequestDTO.getHolders(), ticketType);
            }

            if(eventEntity.getAgeRestriction() > 0){
                checkHolderAgeRestriction(ticketRequestDTO.getHolders(), eventEntity.getAgeRestriction());
            }


            TicketRequest ticketRequest = ticketTypeMapper.fromTicketTypeEntityToTicketRequest(ticketType);
            ticketRequest.setQuantity(quantityHolders);
            ticketRequests.add(ticketRequest);
        });

        PayerRequest payerRequest = payerMapper.fromPayerRequestDTOToPayerRequest(buyTicketRequestDTO.getPayer());

        BuyTicketRequest buyTicketRequest = new BuyTicketRequest();
        buyTicketRequest.setTickets(ticketRequests);
        buyTicketRequest.setPayer(payerRequest);

        // envia os dados para o mercado pago
        BuyTicketResponse response = paymentGateway.createPreference(buyTicketRequest);

        // salvando dados no bdd
        OrderEntity orderEntity = OrderEntity.of(
                response.getId(),
                response.getTotalPrice()
        );
        orderRepository.save(orderEntity);

        buyTicketRequestDTO.getTickets().forEach(ticketRequestDTO -> {
            TicketTypeEntity ticketType = ticketTypeRepository.findById(ticketRequestDTO.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Ingresso não encontrado para compra")
            );

            ticketRequestDTO.getHolders().forEach(holderDTO -> {
                HolderEntity holderEntity = holderMapper.fromHolderCreateRequestDTOToHolderEntity(holderDTO);
                holderRepository.save(holderEntity);


                PaymentStatusEntity paymentStatus = paymentStatusRepository.findByDescription(PaymentStatusEnum.APPROVED.getName());

                TicketEntity ticketEntity = TicketEntity.builder()
                        .ticketTypeEntity(ticketType)
                        .paymentStatusEntity(paymentStatus)
                        .orderEntity(orderEntity)
                        .holderEntity(holderEntity)
                        .build();

                ticketRepository.save(ticketEntity);
            });
        });

        return BuyTicketResponseDTO.builder()
                .orderId(orderEntity.getId().toString())
                .redirectUrl(response.getRedirectUrl())
                .build();
    }


    private void checkHolderExists(List<HolderCreateRequestDTO> holders, TicketTypeEntity ticketTypeEntity) {

        Map<String, String> holderExists = new HashMap<>();
        holders.forEach(holder -> {
            String holderName = holder.getName();
            String holderEmail = holder.getEmail();

            if(!holderRepository.findAll(HolderSpecification.byTicketType(ticketTypeEntity.getId())).isEmpty()){
                throw new BusinessException("O titular já possui ingresso para o evento.");
            }

            if(holderExists.containsKey(holderName) && holderExists.get(holderName).equals(holderEmail)){
                throw new BusinessException("Não é possível comprar mais de um ingresso para o mesmo titular.");
            }else{
                holderExists.put(holderName, holderEmail);
            }
        });
    }

    private void checkHolderAgeRestriction(List<HolderCreateRequestDTO> holders, int ageRestriction) {
        holders.forEach(holder -> {
            long ageHolder = ChronoUnit.YEARS.between(LocalDate.now(), holder.getBirthDate());

            if(ageHolder > ageRestriction){
                throw new BusinessException("O titular do ingresso não possue a idade necessária para o evento.");
            }
        });
    }

    private void checkCapacityEvent (TicketTypeEntity ticketTypeEntity, int countHolders){
        long countTicketsBuy = ticketTypeEntity.getTicketEntities().stream().filter(
                ticket -> ticket.getPaymentStatusEntity().getDescription().equals(PaymentStatusEnum.APPROVED.getName())
        ).count();

        if ((countTicketsBuy + countHolders) > ticketTypeEntity.getCapacity()) {
            throw new BusinessException("A quantidade de ingressos da compra excede a quantidade disponível.");
        }
    }

}
