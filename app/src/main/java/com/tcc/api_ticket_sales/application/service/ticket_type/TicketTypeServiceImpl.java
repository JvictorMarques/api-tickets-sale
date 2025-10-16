package com.tcc.api_ticket_sales.application.service.ticket_type;

import com.tcc.api_ticket_sales.application.exception.EventClosedException;
import com.tcc.api_ticket_sales.application.exception.TicketTypeAlreadyExistsException;
import com.tcc.api_ticket_sales.application.exception.TicketTypeCapacityExceedsEventLimitException;
import com.tcc.api_ticket_sales.application.exception.TicketTypeDatesExceedsEventDateException;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket_type.TicketTypeRepository;
import com.tcc.api_ticket_sales.interfaces.dto.ticket_type.TicketTypeCreateRequestDTO;
import com.tcc.api_ticket_sales.interfaces.dto.ticket_type.TicketTypeCreateResponseDTO;
import com.tcc.api_ticket_sales.interfaces.mapper.ticket_type.TicketTypeMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;

    private final EventRepository eventRepository;

    private final TicketTypeMapper ticketTypeMapper;

    @Override
    @Transactional
    public TicketTypeCreateResponseDTO create(UUID eventId, TicketTypeCreateRequestDTO dto){
        EventEntity eventEntity = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));

        if(eventEntity.isClosed()){
            throw new EventClosedException();
        }

        List<TicketTypeEntity> ticketEntities = ticketTypeRepository.findByEventEntityId(eventId);
        int sumCapacity = ticketEntities.stream().mapToInt(TicketTypeEntity::getCapacity).sum();
        if((sumCapacity + dto.getCapacity()) > eventEntity.getCapacity()){
            int availableCapacity = eventEntity.getCapacity() - sumCapacity;
            throw new TicketTypeCapacityExceedsEventLimitException(availableCapacity);
        }

        if(dto.getDateInitial().isAfter(eventEntity.getDateFinal()) ||
                (dto.getDateFinal() != null && dto.getDateFinal().isAfter(eventEntity.getDateFinal())
                )){
            throw new TicketTypeDatesExceedsEventDateException();
        }

        if(!ticketTypeRepository.findByEventEntityIdAndNameIgnoreCase(eventId, dto.getName()).isEmpty()){
            throw new TicketTypeAlreadyExistsException();
        }


        if(dto.getDateFinal() == null){
            dto.setDateFinal(eventEntity.getDateFinal());
        }
        TicketTypeEntity entitySaved = ticketTypeRepository.save(ticketTypeMapper.fromTicketTypeCreateRequestDTOToTicketTypeEntity(dto, eventId));
        return ticketTypeMapper.fromTicketTypeEntityToTicketTypeCreateResponseDTO(entitySaved);
    }
}
