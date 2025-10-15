package com.tcc.api_ticket_sales.application.service.ticket;

import com.tcc.api_ticket_sales.application.exception.*;
import com.tcc.api_ticket_sales.domain.entity.EventEntity;
import com.tcc.api_ticket_sales.domain.entity.TicketEntity;
import com.tcc.api_ticket_sales.infrastructure.repository.event.EventRepository;
import com.tcc.api_ticket_sales.infrastructure.repository.ticket.TicketRepository;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.TicketCreateRequestDTO;
import com.tcc.api_ticket_sales.interfaces.dto.ticket.TicketCreateResponseDTO;
import com.tcc.api_ticket_sales.interfaces.mapper.ticket.TicketMapper;
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

import static com.tcc.api_ticket_sales.factory.TicketFactory.createTicketCreateResponseDTODefault;
import static com.tcc.api_ticket_sales.factory.TicketFactory.createTicketEntityWithoutId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventEntity eventEntity;
    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketServiceImpl ticketServiceImpl;

    @Test
    @Tag("unit")
    void create_shouldThrowException_whenEventNotFound() {
        // arrange
        when(eventRepository.findById(any())).thenReturn(Optional.empty());
        TicketCreateRequestDTO dto = new TicketCreateRequestDTO();

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
        TicketCreateRequestDTO dto = new TicketCreateRequestDTO();

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

        when(ticketRepository.findByEventEntityId(any())).thenReturn(List.of());

        TicketCreateRequestDTO dto = new TicketCreateRequestDTO();
        dto.setCapacity(11);

        // action e assert
        assertThrows(
                TicketCapacityExceedsEventLimitException.class,
                () -> ticketServiceImpl.create(UUID.randomUUID(), dto)
        );
    }

    @Test
    @Tag("unit")
    void create_shouldThrowException_whenTotalTicketCapacityExceedsEventCapacity() {
        // arrange
        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        when(eventEntity.isClosed()).thenReturn(false);
        when(eventEntity.getCapacity()).thenReturn(20);

        TicketEntity ticketEntity = createTicketEntityWithoutId();
        ticketEntity.setCapacity(15);
        when(ticketRepository.findByEventEntityId(any())).thenReturn(List.of(ticketEntity));

        TicketCreateRequestDTO dto = new TicketCreateRequestDTO();
        dto.setCapacity(10);

        // action e assert
        assertThrows(
                TicketCapacityExceedsEventLimitException.class,
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
        when(ticketRepository.findByEventEntityId(any())).thenReturn(List.of());
        when(eventEntity.getDateFinal()).thenReturn(date);

        TicketCreateRequestDTO dto = new TicketCreateRequestDTO();
        dto.setCapacity(eventEntity.getCapacity());
        dto.setDateInitial(date.plusDays(1));

        // action e assert
        assertThrows(
                TicketDatesExceedsEventDateException.class,
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
        when(ticketRepository.findByEventEntityId(any())).thenReturn(List.of());
        when(eventEntity.getDateFinal()).thenReturn(date);

        TicketCreateRequestDTO dto = new TicketCreateRequestDTO();
        dto.setCapacity(eventEntity.getCapacity());
        dto.setDateInitial(date.minusDays(1));
        dto.setDateFinal(date.plusDays(1));

        // action e assert
        assertThrows(
                TicketDatesExceedsEventDateException.class,
                () -> ticketServiceImpl.create(UUID.randomUUID(), dto)
        );
    }

    @Test
    @Tag("unit")
    void create_shouldThrowException_whenTicketAlreadyExists() {
        // arrange
        LocalDateTime date = LocalDateTime.now();
        TicketEntity ticketEntity = createTicketEntityWithoutId();

        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        when(eventEntity.isClosed()).thenReturn(false);
        when(ticketRepository.findByEventEntityId(any())).thenReturn(List.of());
        when(eventEntity.getDateFinal()).thenReturn(date);
        when(ticketRepository.findByEventEntityIdAndNameIgnoreCase(any(), any())).thenReturn(List.of(ticketEntity));

        TicketCreateRequestDTO dto = new TicketCreateRequestDTO();
        dto.setCapacity(eventEntity.getCapacity());
        dto.setDateInitial(date.minusDays(1));
        dto.setName(ticketEntity.getName());

        // action e assert
        assertThrows(
                TicketAlreadyExistsException.class,
                () -> ticketServiceImpl.create(UUID.randomUUID(), dto)
        );
    }

    @Test
    @Tag("unit")
    void create_shouldReturnTicketCreateResponseDTO_whenTicketCreateRequestDTOIsOK_AndDateFinalIsNull() {
        // arrange
        LocalDateTime date = LocalDateTime.now();
        TicketEntity ticketEntity = createTicketEntityWithoutId();
        TicketCreateResponseDTO mock = createTicketCreateResponseDTODefault();

        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        when(eventEntity.isClosed()).thenReturn(false);
        when(ticketRepository.findByEventEntityId(any())).thenReturn(List.of());
        when(eventEntity.getDateFinal()).thenReturn(date);
        when(ticketRepository.findByEventEntityIdAndNameIgnoreCase(any(), any())).thenReturn(List.of());

        TicketCreateRequestDTO dto = new TicketCreateRequestDTO();
        dto.setCapacity(eventEntity.getCapacity());
        dto.setDateInitial(date.minusDays(1));

        when(ticketRepository.save(any())).thenReturn(ticketEntity);
        when(ticketMapper.fromTicketEntityToTicketCreateResponseDTO(any())).thenReturn(mock);

        // action e assert
        TicketCreateResponseDTO response =  ticketServiceImpl.create(UUID.randomUUID(), dto);
        assertEquals(
                response.getId(),
                mock.getId()
        );
    }

    @Test
    @Tag("unit")
    void create_shouldReturnTicketCreateResponseDTO_whenTicketCreateRequestDTOIsOK_AndDateFinalIsNotNull() {
        // arrange
        LocalDateTime date = LocalDateTime.now();
        TicketEntity ticketEntity = createTicketEntityWithoutId();
        TicketCreateResponseDTO mock = createTicketCreateResponseDTODefault();

        when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        when(eventEntity.isClosed()).thenReturn(false);
        when(ticketRepository.findByEventEntityId(any())).thenReturn(List.of());
        when(eventEntity.getDateFinal()).thenReturn(date);
        when(ticketRepository.findByEventEntityIdAndNameIgnoreCase(any(), any())).thenReturn(List.of());

        TicketCreateRequestDTO dto = new TicketCreateRequestDTO();
        dto.setCapacity(eventEntity.getCapacity());
        dto.setDateInitial(date.minusDays(1));
        dto.setDateFinal(date.minusDays(1));

        when(ticketRepository.save(any())).thenReturn(ticketEntity);
        when(ticketMapper.fromTicketEntityToTicketCreateResponseDTO(any())).thenReturn(mock);

        // action e assert
        TicketCreateResponseDTO response =  ticketServiceImpl.create(UUID.randomUUID(), dto);
        assertEquals(
                response.getId(),
                mock.getId()
        );
    }
}