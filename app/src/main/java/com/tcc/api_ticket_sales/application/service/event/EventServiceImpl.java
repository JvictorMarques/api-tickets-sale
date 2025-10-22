package com.tcc.api_ticket_sales.application.service.event;

import com.tcc.api_ticket_sales.application.exception.EventAlreadyExistsException;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepository;
import com.tcc.api_ticket_sales.application.dto.event.EventCreateDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventResponseDTO;
import com.tcc.api_ticket_sales.application.mapper.event.EventMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    @Transactional
    public EventResponseDTO createEvent(EventCreateDTO dto){
        if(eventRepository.checkExists(dto)){
            throw new EventAlreadyExistsException();
        }
        EventEntity entity = eventMapper.fromEventCreateDTOToEventEntity(dto);

        EventEntity entitySaved = eventRepository.save(entity);
        return eventMapper.fromEventEntityToEventResponseDTO(entitySaved);
    }
}
