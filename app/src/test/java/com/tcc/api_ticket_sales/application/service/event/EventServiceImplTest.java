package com.tcc.api_ticket_sales.application.service.event;

import com.tcc.api_ticket_sales.application.exception.EventAlreadyExistsException;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepository;
import com.tcc.api_ticket_sales.interfaces.dto.event.EventCreateDTO;
import com.tcc.api_ticket_sales.interfaces.dto.event.EventResponseDTO;
import com.tcc.api_ticket_sales.interfaces.mapper.event.EventMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithoutId;
import static com.tcc.api_ticket_sales.factory.EventFactory.createEventResponseDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventServiceImpl eventServiceImpl;

    @Test
    public void createEvent_shouldThrowException_whenEventExists() {
        // arrange
        when(eventRepository.checkExists(any())).thenReturn(true);
        EventCreateDTO dto = new EventCreateDTO();

        // action e assert
        assertThrows(
                EventAlreadyExistsException.class,
                () -> eventServiceImpl.createEvent(dto)
        );
    }

    @Test
    public void createEvent_shouldReturnEventResponseDTO_whenEventNotExists() {
        // arrange
        EventResponseDTO eventMock = createEventResponseDTO();
        EventEntity eventEntity = createEventEntityWithoutId();

        when(eventMapper.fromEventCreateDTOToEventEntity(any())).thenReturn(eventEntity);
        when(eventRepository.checkExists(any())).thenReturn(false);
        when(eventRepository.save(any())).thenReturn(eventEntity);
        when(eventMapper.fromEventEntityToEventResponseDTO(any())).thenReturn(eventMock);

        // action
        EventResponseDTO eventResponseDTO = eventServiceImpl.createEvent(new  EventCreateDTO());

        // assert
        assertEquals(eventMock.getId(), eventResponseDTO.getId());
    }
}