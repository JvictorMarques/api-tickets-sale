package com.tcc.api_ticket_sales.application.service.buy_ticket;

import com.tcc.api_ticket_sales.application.dto.buy_ticket.BuyTicketRequestDTO;
import com.tcc.api_ticket_sales.application.dto.holder.HolderCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.ticket.TicketCreateRequestDTO;
import com.tcc.api_ticket_sales.application.exception.BuyTicketHolderDuplicateRequestException;
import com.tcc.api_ticket_sales.application.exception.TicketTypeNotFoundException;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.service.TicketTypeDomainService;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket_type.TicketTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BuyTicketValidator {
    private final TicketTypeDomainService ticketTypeDomainService;
    private final TicketTypeRepository ticketTypeRepository;

    public void validate(BuyTicketRequestDTO buyTicketRequestDTO) {
        checkDuplicateHolders(buyTicketRequestDTO);
        validateTicketTypes(buyTicketRequestDTO);
    }

    private void checkDuplicateHolders(BuyTicketRequestDTO buyTicketRequestDTO) {
        for (TicketCreateRequestDTO ticketRequest: buyTicketRequestDTO.getTickets()){
            Map<String, String> holderExists = new HashMap<>();
            for(HolderCreateRequestDTO holderRequest: ticketRequest.getHolders()){
                String holderName = holderRequest.getName();
                String holderEmail = holderRequest.getEmail();

                if(holderExists.containsKey(holderName)
                        && holderExists.get(holderName).equals(holderEmail)){
                    throw new BuyTicketHolderDuplicateRequestException(holderName);
                }else{
                    holderExists.put(holderName, holderEmail);
                }
            }
        }
    }

    private void validateTicketTypes(BuyTicketRequestDTO buyTicketRequestDTO) {
        Map<UUID, Integer> quantityByTicket = new HashMap<>();
        for (TicketCreateRequestDTO ticketRequest: buyTicketRequestDTO.getTickets()){
            TicketTypeEntity ticketType = ticketTypeRepository.findById(ticketRequest.getId()).orElseThrow(
                    () -> new TicketTypeNotFoundException(ticketRequest.getId().toString())
            );

            int quantityHolders = ticketRequest.getHolders().size();
            quantityByTicket.merge(ticketRequest.getId(), quantityHolders, Integer::sum);

            ticketTypeDomainService.validateCapacity(ticketType, quantityByTicket.get(ticketRequest.getId()));
        }
    }
}
