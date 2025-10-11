package com.tcc.api_ticket_sales.application.service.ticket;

import com.tcc.api_ticket_sales.application.exception.EventClosedException;
import com.tcc.api_ticket_sales.application.exception.TicketAlreadyExistsException;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket.TicketRepository;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.TicketCreateRequestDTO;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.TicketCreateResponseDTO;
import com.tcc.api_ticket_sales.interfaces.mapper.ticket.TicketMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    private final EventRepository eventRepository;

    private final TicketMapper ticketMapper;

    @Override
    @Transactional
    public TicketCreateResponseDTO create(UUID eventId, TicketCreateRequestDTO dto){
        EventEntity eventEntity = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));

        if(eventEntity.isClosed()){
            throw new EventClosedException();
        }

        List<TicketEntity> ticketEntities = ticketRepository.findByEventEntityId(eventId);
        int sumCapacity = ticketEntities.stream().mapToInt(TicketEntity::getCapacity).sum();
        if(sumCapacity >= eventEntity.getCapacity()){
            throw new BusinessException("O evento já possui ingressos com a capacidade necessária.");
        }
        if((sumCapacity+dto.getCapacity()) > eventEntity.getCapacity()){
            throw new BusinessException("A capacidade do ingresso excede o permitido no evento");
        }

        if(dto.getDateInitial().isAfter(eventEntity.getDateFinal()) ||
                (dto.getDateFinal() != null && dto.getDateFinal().isAfter(eventEntity.getDateFinal())
                )){
            throw new BusinessException("A data de venda dos ingressos não pode ser após o evento.");
        }

        if(!ticketRepository.findByEventEntityIdAndNameIgnoreCase(eventId, dto.getName()).isEmpty()){
            throw new TicketAlreadyExistsException();
        }

        if(dto.getCapacity() > eventEntity.getCapacity()){
            throw new BusinessException("A capacidade de pessoas que podem adquirir o ingresso deve ser inferior ou igual a do evento.");
        }

        if(dto.getDateFinal() == null){
            dto.setDateFinal(eventEntity.getDateFinal());
        }
        TicketEntity entitySaved = ticketRepository.save(ticketMapper.fromTicketCreateRequestDTOToTicketEntity(dto, eventId));
        return ticketMapper.fromTicketEntityToTicketCreateResponseDTO(entitySaved);
    }
}
