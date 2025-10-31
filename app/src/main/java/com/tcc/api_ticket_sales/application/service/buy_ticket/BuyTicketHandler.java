package com.tcc.api_ticket_sales.application.service.buy_ticket;

import com.tcc.api_ticket_sales.application.dto.holder.HolderCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.payment.ItemPaymentRequestDTO;
import com.tcc.api_ticket_sales.application.dto.ticket.TicketCreateRequestDTO;
import com.tcc.api_ticket_sales.application.exception.TicketTypeNotFoundException;
import com.tcc.api_ticket_sales.application.mapper.holder.HolderMapper;
import com.tcc.api_ticket_sales.domain.entity.HolderEntity;
import com.tcc.api_ticket_sales.domain.entity.OrderEntity;
import com.tcc.api_ticket_sales.domain.entity.PaymentStatusEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.service.HolderDomainService;
import com.tcc.api_ticket_sales.domain.service.TicketDomainService;
import com.tcc.api_ticket_sales.infrastructure.repository.holder.HolderRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket.TicketRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket_type.TicketTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BuyTicketHandler {

    private final TicketTypeRepository ticketTypeRepository;
    private final TicketDomainService ticketDomainService;
    private final TicketRepository ticketRepository;
    private final HolderRepository holderRepository;
    private final HolderDomainService holderDomainService;
    private final HolderMapper holderMapper;

    public List<ItemPaymentRequestDTO> processTickets(
          List<TicketCreateRequestDTO> tickets,
          OrderEntity orderEntity,
          PaymentStatusEntity paymentStatusEntity
    ){
       List<ItemPaymentRequestDTO> items = new ArrayList<>();

       for(TicketCreateRequestDTO ticketRequestDTO : tickets){
           TicketTypeEntity ticketType = ticketTypeRepository.findById(ticketRequestDTO.getId()).orElseThrow(
                   () -> new TicketTypeNotFoundException(ticketRequestDTO.getId().toString())
           );

           for(HolderCreateRequestDTO holderRequestDTO : ticketRequestDTO.getHolders()){
               HolderEntity holderEntity = holderDomainService.creatOrReturnExistsHolderEntity(
                       holderMapper.fromHolderCreateRequestDTOToHolderEntity(holderRequestDTO),
                       holderRepository.findByNameAndEmail(holderRequestDTO.getName(), holderRequestDTO.getEmail())
               );

               holderRepository.save(holderEntity);

               TicketEntity ticketEntity = ticketDomainService.createTicket(
                       ticketType, holderEntity, orderEntity, paymentStatusEntity
               );
               ticketRepository.save(ticketEntity);
           }

           items.add(ItemPaymentRequestDTO.builder()
                   .id(ticketType.getId().toString())
                   .title(ticketType.getName())
                   .description(ticketType.getDescription())
                   .unitPrice(ticketType.getPrice())
                   .quantity(ticketRequestDTO.getHolders().size())
                   .build());
       }

       return items;
    }
}
