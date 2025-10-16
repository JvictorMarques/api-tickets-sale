package com.tcc.api_ticket_sales.application.service.ticket_type;

import com.tcc.api_ticket_sales.application.exception.*;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketTypeEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket_type.TicketTypeRepository;
import com.tcc.api_ticket_sales.interfaces.dto.ticket_type.TicketTypeCreateRequestDTO;
import com.tcc.api_ticket_sales.interfaces.dto.ticket_type.TicketTypeCreateResponseDTO;
import com.tcc.api_ticket_sales.interfaces.mapper.ticket_type.TicketTypeMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeCreateResponseDTODefault;
import static com.tcc.api_ticket_sales.factory.TicketTypeFactory.createTicketTypeEntityWithoutId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketTypeServiceImplTest {

    @Mock
    private TicketTypeRepository ticketTypeRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventEntity eventEntity;
    @Mock
    private TicketTypeMapper ticketTypeMapper;

    @InjectMocks
    private TicketTypeServiceImpl ticketServiceImpl;

    @Test
    @Tag("unit")
    void create_shouldThrowException_whenEventNotFound() {
        // arrange
        when(eventRepository.findById(any())).thenReturn(Optional.empty());
        TicketTypeCreateRequestDTO dto = new TicketTypeCreateRequestDTO();

        // action e assert
        assertThrows(
                EntityNotFoundException.class,
                () -> ticketServiceImpl.create(UUID.randomUUID(), dto)
        );
    }

    @Test
    @Tag("unit")
    void create_shouldThrowException_whenEventIsClosed() {
        // arrange
        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        when(eventEntity.isClosed()).thenReturn(true);
        TicketTypeCreateRequestDTO dto = new TicketTypeCreateRequestDTO();

        // action e assert
        assertThrows(
                EventClosedException.class,
                () -> ticketServiceImpl.create(UUID.randomUUID(), dto)
        );
    }

    @Test
    @Tag("unit")
    void create_shouldThrowException_whenTicketCapacityExceedsEventCapacity() {
        // arrange
        eventEntity.setCapacity(10);
        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        when(eventEntity.isClosed()).thenReturn(false);

        when(ticketTypeRepository.findByEventEntityId(any())).thenReturn(List.of());

        TicketTypeCreateRequestDTO dto = new TicketTypeCreateRequestDTO();
        dto.setCapacity(11);

        // action e assert
        assertThrows(
                TicketTypeCapacityExceedsEventLimitException.class,
                () -> ticketServiceImpl.create(UUID.randomUUID(), dto)
        );
    }

    @Test
    @Tag("unit")
    void create_shouldThrowException_whenTotalTicketTypeCapacityExceedsEventCapacity() {
        // arrange
        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        when(eventEntity.isClosed()).thenReturn(false);
        when(eventEntity.getCapacity()).thenReturn(20);

        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithoutId();
        ticketTypeEntity.setCapacity(15);
        when(ticketTypeRepository.findByEventEntityId(any())).thenReturn(List.of(ticketTypeEntity));

        TicketTypeCreateRequestDTO dto = new TicketTypeCreateRequestDTO();
        dto.setCapacity(10);

        // action e assert
        assertThrows(
                TicketTypeCapacityExceedsEventLimitException.class,
                () -> ticketServiceImpl.create(UUID.randomUUID(), dto)
        );
    }

    @Test
    @Tag("unit")
    void create_shouldThrowException_whenDateInitialGreaterThanEventDateFinal() {
        // arrange
        LocalDateTime date = LocalDateTime.now();
        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        when(eventEntity.isClosed()).thenReturn(false);
        when(ticketTypeRepository.findByEventEntityId(any())).thenReturn(List.of());
        when(eventEntity.getDateFinal()).thenReturn(date);

        TicketTypeCreateRequestDTO dto = new TicketTypeCreateRequestDTO();
        dto.setCapacity(eventEntity.getCapacity());
        dto.setDateInitial(date.plusDays(1));

        // action e assert
        assertThrows(
                TicketTypeDatesExceedsEventDateException.class,
                () -> ticketServiceImpl.create(UUID.randomUUID(), dto)
        );
    }

    @Test
    @Tag("unit")
    void create_shouldThrowException_whenDateFinalGreaterThanEventDateFinal() {
        // arrange
        LocalDateTime date = LocalDateTime.now();
        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        when(eventEntity.isClosed()).thenReturn(false);
        when(ticketTypeRepository.findByEventEntityId(any())).thenReturn(List.of());
        when(eventEntity.getDateFinal()).thenReturn(date);

        TicketTypeCreateRequestDTO dto = new TicketTypeCreateRequestDTO();
        dto.setCapacity(eventEntity.getCapacity());
        dto.setDateInitial(date.minusDays(1));
        dto.setDateFinal(date.plusDays(1));

        // action e assert
        assertThrows(
                TicketTypeDatesExceedsEventDateException.class,
                () -> ticketServiceImpl.create(UUID.randomUUID(), dto)
        );
    }

    @Test
    @Tag("unit")
    void create_shouldThrowException_whenTicketTypeAlreadyExists() {
        // arrange
        LocalDateTime date = LocalDateTime.now();
        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithoutId();

        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        when(eventEntity.isClosed()).thenReturn(false);
        when(ticketTypeRepository.findByEventEntityId(any())).thenReturn(List.of());
        when(eventEntity.getDateFinal()).thenReturn(date);
        when(ticketTypeRepository.findByEventEntityIdAndNameIgnoreCase(any(), any())).thenReturn(List.of(ticketTypeEntity));

        TicketTypeCreateRequestDTO dto = new TicketTypeCreateRequestDTO();
        dto.setCapacity(eventEntity.getCapacity());
        dto.setDateInitial(date.minusDays(1));
        dto.setName(ticketTypeEntity.getName());

        // action e assert
        assertThrows(
                TicketTypeAlreadyExistsException.class,
                () -> ticketServiceImpl.create(UUID.randomUUID(), dto)
        );
    }

    @Test
    @Tag("unit")
    void create_shouldReturnTicketCreateResponseDTO_whenTicketTypeCreateRequestDTOIsOK_AndDateFinalIsNull() {
        // arrange
        LocalDateTime date = LocalDateTime.now();
        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithoutId();
        TicketTypeCreateResponseDTO mock = createTicketTypeCreateResponseDTODefault();

        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        when(eventEntity.isClosed()).thenReturn(false);
        when(ticketTypeRepository.findByEventEntityId(any())).thenReturn(List.of());
        when(eventEntity.getDateFinal()).thenReturn(date);
        when(ticketTypeRepository.findByEventEntityIdAndNameIgnoreCase(any(), any())).thenReturn(List.of());

        TicketTypeCreateRequestDTO dto = new TicketTypeCreateRequestDTO();
        dto.setCapacity(eventEntity.getCapacity());
        dto.setDateInitial(date.minusDays(1));

        when(ticketTypeRepository.save(any())).thenReturn(ticketTypeEntity);
        when(ticketTypeMapper.fromTicketTypeEntityToTicketTypeCreateResponseDTO(any())).thenReturn(mock);

        // action e assert
        TicketTypeCreateResponseDTO response =  ticketServiceImpl.create(UUID.randomUUID(), dto);
        assertEquals(
                response.getId(),
                mock.getId()
        );
    }

    @Test
    @Tag("unit")
    void create_shouldReturnTicketCreateResponseDTO_whenTicketTypeCreateRequestDTOIsOK_AndDateFinalIsNotNull() {
        // arrange
        LocalDateTime date = LocalDateTime.now();
        TicketTypeEntity ticketTypeEntity = createTicketTypeEntityWithoutId();
        TicketTypeCreateResponseDTO mock = createTicketTypeCreateResponseDTODefault();

        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        when(eventEntity.isClosed()).thenReturn(false);
        when(ticketTypeRepository.findByEventEntityId(any())).thenReturn(List.of());
        when(eventEntity.getDateFinal()).thenReturn(date);
        when(ticketTypeRepository.findByEventEntityIdAndNameIgnoreCase(any(), any())).thenReturn(List.of());

        TicketTypeCreateRequestDTO dto = new TicketTypeCreateRequestDTO();
        dto.setCapacity(eventEntity.getCapacity());
        dto.setDateInitial(date.minusDays(1));
        dto.setDateFinal(date.minusDays(1));

        when(ticketTypeRepository.save(any())).thenReturn(ticketTypeEntity);
        when(ticketTypeMapper.fromTicketTypeEntityToTicketTypeCreateResponseDTO(any())).thenReturn(mock);

        // action e assert
        TicketTypeCreateResponseDTO response =  ticketServiceImpl.create(UUID.randomUUID(), dto);
        assertEquals(
                response.getId(),
                mock.getId()
        );
    }
}