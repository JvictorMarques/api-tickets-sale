package com.tcc.api_ticket_sales.application.service.event;

import com.tcc.api_ticket_sales.application.dto.event.EventUpdateRequestDTO;
import com.tcc.api_ticket_sales.application.exception.EventAlreadyExistsException;
import com.tcc.api_ticket_sales.application.exception.EventNotFoundException;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.service.EventDomainService;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepository;
import com.tcc.api_ticket_sales.application.dto.event.EventCreateRequestDTO;
import com.tcc.api_ticket_sales.application.dto.event.EventResponseDTO;
import com.tcc.api_ticket_sales.application.mapper.event.EventMapper;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventSpecification;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventSpecificationFactory;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.tcc.api_ticket_sales.factory.EventFactory.createEventEntityWithoutId;
import static com.tcc.api_ticket_sales.factory.EventFactory.createEventResponseDTO;
import static com.tcc.api_ticket_sales.factory.EventFactory.createEventUpdateRequestDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventSpecificationFactory eventSpecificationFactory;

    @Mock
    private EventDomainService eventDomainService;

    @InjectMocks
    private EventServiceImpl eventServiceImpl;

    @Test
    @Tag("unit")
    void create_shouldThrowException_whenExists() {
        // arrange
        EventEntity eventEntity = createEventEntityWithoutId();
        Specification<EventEntity> mockSpec = mock(Specification.class);
        EventCreateRequestDTO dto = new EventCreateRequestDTO();

        when(eventMapper.fromEventCreateRequestDTOToEventEntity(any())).thenReturn(eventEntity);
        when(eventSpecificationFactory.findConflictingEvents(eventEntity)).thenReturn(mockSpec);
        when(eventRepository.findAll(mockSpec)).thenReturn(List.of(eventEntity));

        // action e assert
        assertThrows(
                EventAlreadyExistsException.class,
                () -> eventServiceImpl.create(dto)
        );
    }

    @Test
    @Tag("unit")
    void create_shouldReturnEventResponseDTO_whenNotExists() {
        // arrange
        EventResponseDTO eventMock = createEventResponseDTO();
        EventEntity eventEntity = createEventEntityWithoutId();
        Specification<EventEntity> mockSpec = mock(Specification.class);

        when(eventMapper.fromEventCreateRequestDTOToEventEntity(any())).thenReturn(eventEntity);
        when(eventSpecificationFactory.findConflictingEvents(eventEntity)).thenReturn(mockSpec);
        when(eventRepository.findAll(mockSpec)).thenReturn(List.of());
        when(eventRepository.save(any())).thenReturn(eventEntity);
        when(eventMapper.fromEventEntityToEventResponseDTO(any())).thenReturn(eventMock);

        // action
        EventResponseDTO eventResponseDTO = eventServiceImpl.create(new EventCreateRequestDTO());

        // assert
        assertEquals(eventMock.getId(), eventResponseDTO.getId());
    }

    @Tag("unit")
    @Test
    void update_shouldThrowEventNotFoundException_whenEventNotExists(){
        UUID eventId = UUID.randomUUID();
        EventUpdateRequestDTO eventUpdateRequestDTO = createEventUpdateRequestDTO();
        when(eventRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> {
            eventServiceImpl.update(eventId, eventUpdateRequestDTO);
        });
    }

    @Tag("unit")
    @Test
    void update_shouldThrowEventAlreadyExistsException_whenEventAlreadyExists(){
        EventEntity eventEntity = createEventEntityWithoutId();
        EventUpdateRequestDTO eventUpdateRequestDTO = createEventUpdateRequestDTO();
        Specification<EventEntity> mockSpec = mock(Specification.class);

        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        when(eventMapper.fromEventUpdateRequestDTOToEventEntity(any(), any())).thenReturn(eventEntity);
        when(eventSpecificationFactory.findConflictingEvents(eventEntity)).thenReturn(mockSpec);
        when(eventRepository.findAll(mockSpec)).thenReturn(List.of(eventEntity));

        assertThrows(EventAlreadyExistsException.class, () -> {
            eventServiceImpl.update(eventEntity.getId(), eventUpdateRequestDTO);
        });
    }

    @Tag("unit")
    @Test
    void update_shouldReturnEventResponseDTO_whenUpdateEventSuccess(){
        EventEntity eventEntity = createEventEntityWithoutId();
        EventUpdateRequestDTO eventUpdateRequestDTO = createEventUpdateRequestDTO();
        Specification<EventEntity> mockSpec = mock(Specification.class);
        EventResponseDTO eventResponseDTO = createEventResponseDTO();

        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        when(eventMapper.fromEventUpdateRequestDTOToEventEntity(any(), any())).thenReturn(eventEntity);
        when(eventSpecificationFactory.findConflictingEvents(eventEntity)).thenReturn(mockSpec);
        when(eventRepository.findAll(mockSpec)).thenReturn(List.of());
        doNothing().when(eventDomainService).updateEvent(any(), any());
        when(eventRepository.save(any())).thenReturn(eventEntity);
        when(eventMapper.fromEventEntityToEventResponseDTO(any())).thenReturn(eventResponseDTO);

        EventResponseDTO response = eventServiceImpl.update(eventEntity.getId(), eventUpdateRequestDTO);
        assertEquals(response.getId(), eventResponseDTO.getId());
    }

    @Tag("unit")
    @Test
    void delete_shouldThrowEventNotFoundException_whenEventNotExists(){
        UUID eventId = UUID.randomUUID();
        when(eventRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> {
            eventServiceImpl.delete(eventId);
        });
    }

    @Tag("unit")
    @Test
    void delete_shouldThrowEventNotFoundException_whenEventIsDeleted(){
        UUID eventId = UUID.randomUUID();
        EventEntity eventEntity = createEventEntityWithoutId();
        eventEntity.setDeletedAt(LocalDateTime.now());
        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));

        assertThrows(EventNotFoundException.class, () -> {
            eventServiceImpl.delete(eventId);
        });
    }

    @Tag("unit")
    @Test
    void delete_shouldReturnVoid_whenSuccess(){
        UUID eventId = UUID.randomUUID();
        EventEntity eventEntity = createEventEntityWithoutId();
        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        doNothing().when(eventDomainService).deletedEvent(eventEntity);
        when(eventRepository.save(any())).thenReturn(eventEntity);

        assertDoesNotThrow(() -> {
            eventServiceImpl.delete(eventId);
        });

        verify(eventRepository).save(eventEntity);
        verify(eventDomainService).deletedEvent(eventEntity);
    }
}