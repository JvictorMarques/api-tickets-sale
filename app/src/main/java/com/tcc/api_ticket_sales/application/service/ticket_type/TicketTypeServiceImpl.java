package com.tcc.api_ticket_sales.application.service.ticket_type;

import com.tcc.api_ticket_sales.application.exception.EventNotFoundException;
import com.tcc.api_ticket_sales.application.exception.TicketTypeAlreadyExistsException;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.domain.service.TicketTypeDomainService;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket_type.TicketTypeRepository;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.ticket_type.TicketTypeCreateResponseDTO;
import com.tcc.api_ticket_sales.application.mapper.ticket_type.TicketTypeMapper;
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

    private final TicketTypeDomainService ticketTypeDomainService;

    @Override
    @Transactional
    public TicketTypeCreateResponseDTO create(UUID eventId, TicketTypeCreateRequestDTO dto){
        EventEntity eventEntity = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId.toString()));

        if(!ticketTypeRepository.findByEventEntityIdAndNameIgnoreCase(eventId, dto.getName()).isEmpty()){
            throw new TicketTypeAlreadyExistsException();
        }

        List<TicketTypeEntity> ticketTypesExists = ticketTypeRepository.findByEventEntityId(eventId);

        TicketTypeEntity ticketTypeEntityMapper = ticketTypeMapper.fromTicketTypeCreateRequestDTOToTicketTypeEntity(dto, eventId);

        TicketTypeEntity ticketTypeEntity = ticketTypeDomainService.createTicketType(eventEntity, ticketTypesExists, ticketTypeEntityMapper);
        ticketTypeRepository.save(ticketTypeEntity);

        return ticketTypeMapper.fromTicketTypeEntityToTicketTypeCreateResponseDTO(ticketTypeEntity);
    }
}
