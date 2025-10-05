package com.tcc.api_ticket_sales.application.service.event;

import com.tcc.api_ticket_sales.application.exception.EventAlreadyExistsException;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.exception.BusinessException;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepository;
import com.tcc.api_ticket_sales.interfaces.dto.event.EventCreateDTO;
import com.tcc.api_ticket_sales.interfaces.dto.event.EventResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.tcc.api_ticket_sales.interfaces.mapper.event.EventMapper.fromEventCreateDTOToEventEntity;
import static com.tcc.api_ticket_sales.interfaces.mapper.event.EventMapper.fromEventEntityToEventResponseDTO;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Transactional
    public EventResponseDTO createEvent(EventCreateDTO dto){
        if(eventRepository.checkExists(dto)){
            throw new EventAlreadyExistsException();
        }
        EventEntity entity = fromEventCreateDTOToEventEntity(dto);

        EventEntity entitySaved = eventRepository.save(entity);
        return fromEventEntityToEventResponseDTO(entitySaved);
    }
}
