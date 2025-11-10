package com.tcc.api_ticket_sales.application.service.event;

import com.tcc.api_ticket_sales.application.dto.event.EventFilterRequestDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventUpdateRequestDTO;
import com.tcc.api_ticket_sales.application.exception.EventAlreadyExistsException;
import com.tcc.api_ticket_sales.application.exception.EventNotFoundException;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.service.EventDomainService;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepository;
import com.tcc.api_ticket_sales.application.dto.event.EventCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventResponseDTO;
import com.tcc.api_ticket_sales.application.mapper.event.EventMapper;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventSpecificationFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.tcc.api_ticket_sales.domain.utils.CheckDate.checkDateInitialGreaterThanDateFinal;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final EventDomainService eventDomainService;

    private final EventSpecificationFactory eventSpecificationFactory;


    @Transactional
    public EventResponseDTO create(EventCreateRequestDTO dto){
        EventEntity entity = eventMapper.fromEventCreateRequestDTOToEventEntity(dto);
        List<EventEntity> exists = eventRepository.findAll(eventSpecificationFactory.findConflictingEvents(entity));
        if(!exists.isEmpty()){
            throw new EventAlreadyExistsException();
        }

        EventEntity entitySaved = eventRepository.save(entity);
        return eventMapper.fromEventEntityToEventResponseDTO(entitySaved);
    }

    @Transactional
    public EventResponseDTO update(UUID eventId, EventUpdateRequestDTO dto){
        EventEntity eventEntityOld= eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId.toString()));
        EventEntity eventEntityNew = eventMapper.fromEventUpdateRequestDTOToEventEntity(dto, eventEntityOld);

        List<EventEntity> eventEntities = eventRepository.findAll(eventSpecificationFactory.findConflictingEvents(eventEntityNew));

        if(!eventEntities.isEmpty()){
            throw new EventAlreadyExistsException();
        }

        eventDomainService.updateEvent(eventEntityOld, eventEntityNew);

        eventRepository.save(eventEntityNew);
        return eventMapper.fromEventEntityToEventResponseDTO(eventEntityNew);
    }

    @Transactional
    public void delete(UUID eventId){
        EventEntity event= eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId.toString()));

        eventDomainService.deletedEvent(event);
        eventRepository.save(event);
    }

    public List<EventResponseDTO> getByParams(EventFilterRequestDTO filter){
        if(filter.getDateInitial() != null || filter.getDateFinal() != null){
            checkDateInitialGreaterThanDateFinal(filter.getDateInitial(), filter.getDateFinal());
        }

        List<EventEntity> eventEntities = eventRepository.findAll(eventSpecificationFactory.findFilter(filter));

        return eventEntities.stream().map(eventMapper::fromEventEntityToEventResponseDTO).toList();
    }

    public EventResponseDTO getById(UUID eventId){
        EventEntity eventEntity= eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId.toString()));

        if(eventEntity.getDeletedAt() != null) throw new EventNotFoundException(eventId.toString());

        return eventMapper.fromEventEntityToEventResponseDTO(eventEntity);
    }
}
